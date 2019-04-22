package com.huikezk.alarmpro.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.utils.MyUtils;

import net.igenius.mqttservice.MQTTServiceLogger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.huikezk.alarmpro.service.MyMQTTCommand.BROADCAST_CONNECTION_STATUS;
import static com.huikezk.alarmpro.service.MyMQTTCommand.BROADCAST_EXCEPTION;
import static com.huikezk.alarmpro.service.MyMQTTCommand.BROADCAST_MESSAGE_ARRIVED;
import static com.huikezk.alarmpro.service.MyMQTTCommand.BROADCAST_PUBLISH_SUCCESS;
import static com.huikezk.alarmpro.service.MyMQTTCommand.BROADCAST_SUBSCRIPTION_ERROR;
import static com.huikezk.alarmpro.service.MyMQTTCommand.BROADCAST_SUBSCRIPTION_SUCCESS;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_AUTO_RESUBSCRIBE_ON_RECONNECT;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_BROADCAST_TYPE;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_BROKER_URL;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_CLIENT_ID;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_CONNECTED;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_EXCEPTION;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_PASSWORD;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_PAYLOAD;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_QOS;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_REQUEST_ID;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_TOPIC;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_TOPICS;
import static com.huikezk.alarmpro.service.MyMQTTCommand.PARAM_USERNAME;
import static com.huikezk.alarmpro.service.MyMQTTCommand.getBroadcastAction;


public class MyMqttService extends MyBackgroudService implements Runnable,MqttCallbackExtended {

    public static String NAMESPACE = "com.huikezk.alarmpro";
    public static int KEEP_ALIVE_INTERVAL = 60; //measured in seconds
    public static int CONNECT_TIMEOUT = 30; //measured in seconds

    private BlockingQueue<Intent> mIntents = new LinkedBlockingQueue<>();
    private MqttClient mClient;
    private boolean mShutdown = false;
    private String mConnectionRequestId = null;
    private HashMap<String, Integer> mTopicsToAutoResubscribe = new LinkedHashMap<>();
    private String TAG="MyMqttService";

    @Override
    public void onCreate() {
        super.onCreate();
        String CHANNEL_ONE_ID = "com.huikezk.alarmpro";
        String CHANNEL_ONE_NAME = "Channel One";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, CHANNEL_ONE_ID)
                    .setContentTitle("智能楼宇系统")
                    .setContentText("智能楼宇系统")
                    .setSmallIcon(R.mipmap.logo)
                    .build();
            startForeground(99, notification);
        } else {
            startForeground(99, getNotification());
        }
    }

    private Notification getNotification() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setShowWhen(false);
        mBuilder.setAutoCancel(false);
        mBuilder.setSmallIcon(R.mipmap.logo);
        mBuilder.setContentText("智能楼宇系统");
        mBuilder.setContentTitle("智能楼宇系统");
        return mBuilder.build();
    }

    private String getParameter(Intent intent, String key) {
        return intent.getStringExtra(key);
    }

    private void broadcast(String type, String requestId, String... params) {
        if (params != null && params.length > 0 && params.length % 2 != 0)
            throw new IllegalArgumentException("Parameters must be passed in the form: PARAM_NAME, paramValue");

        Intent intent = new Intent();

        intent.setAction(getBroadcastAction());
        intent.putExtra(PARAM_BROADCAST_TYPE, type);
        intent.putExtra(PARAM_REQUEST_ID, requestId);

        if (params != null && params.length > 0) {
            for (int i = 0; i <= params.length - 2; i += 2) {
                intent.putExtra(params[i], params[i + 1]);
            }
        }

        sendBroadcast(intent);
    }

    private void broadcastPayload(String type, String requestId, byte[] payload, String topic) {
        Intent intent = new Intent();

        intent.setAction(getBroadcastAction());
        intent.putExtra(PARAM_BROADCAST_TYPE, type);
        intent.putExtra(PARAM_REQUEST_ID, requestId);
        intent.putExtra(PARAM_PAYLOAD, payload);
        intent.putExtra(PARAM_TOPIC, topic);

        sendBroadcast(intent);
    }

    private void broadcastConnectionStatus(String requestId) {
        Intent intent = new Intent();

        intent.setAction(getBroadcastAction());
        intent.putExtra(PARAM_BROADCAST_TYPE, BROADCAST_CONNECTION_STATUS);
        intent.putExtra(PARAM_REQUEST_ID, requestId);
        intent.putExtra(PARAM_CONNECTED, mClient != null && mClient.isConnected());

        sendBroadcast(intent);
    }

    private void broadcastException(String type, String requestId, Exception exception, String... params) {
        Intent intent = new Intent();

        intent.setAction(getBroadcastAction());
        intent.putExtra(PARAM_BROADCAST_TYPE, type);
        intent.putExtra(PARAM_REQUEST_ID, requestId);
        intent.putExtra(PARAM_EXCEPTION, exception);

        if (params != null && params.length > 0) {
            for (int i = 0; i <= params.length - 2; i += 2) {
                intent.putExtra(params[i], params[i + 1]);
            }
        }

        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if (intent != null) {
            if (intent.getAction() == null || intent.getAction().isEmpty()) {
                MQTTServiceLogger.error("MQTTService onStartCommand",
                        "null or empty Intent passed, ignoring it!");
            } else {
                mShutdown = false;
                mIntents.offer(intent);
                post(this);
            }
        }

        if (mShutdown) {
            MQTTServiceLogger.debug(getClass().getSimpleName(), "Shutting down service");
            stopSelf();
            return START_NOT_STICKY;
        }

        return START_STICKY;
    }

    @Override
    public void run() {
        try {
            Intent intent = mIntents.take();
            String action = intent.getAction();
            String requestId = getParameter(intent, PARAM_REQUEST_ID);

            if (MyMQTTCommand.ACTION_CONNECT.equals(action) || MyMQTTCommand.ACTION_CONNECT_AND_SUBSCRIBE.equals(action)) {
                boolean connected = onConnect(requestId, getParameter(intent, PARAM_BROKER_URL),
                        getParameter(intent, PARAM_CLIENT_ID), getParameter(intent, PARAM_USERNAME),
                        getParameter(intent, PARAM_PASSWORD));

                if (MyMQTTCommand.ACTION_CONNECT_AND_SUBSCRIBE.equals(action) && connected) {
                    int qos = getInt(getParameter(intent, PARAM_QOS));
                    String[] topics = intent.getStringArrayExtra(PARAM_TOPICS);
                    boolean autoResubscribe = intent.getBooleanExtra(PARAM_AUTO_RESUBSCRIBE_ON_RECONNECT, false);
                    onSubscribe(requestId, qos, autoResubscribe, topics);
                }

            } else if (MyMQTTCommand.ACTION_DISCONNECT.equals(action)) {
                onDisconnect(requestId);

            } else if (MyMQTTCommand.ACTION_SUBSCRIBE.equals(action)) {
                onSubscribe(requestId, getInt(getParameter(intent, PARAM_QOS)),
                        intent.getBooleanExtra(PARAM_AUTO_RESUBSCRIBE_ON_RECONNECT, false),
                        intent.getStringArrayExtra(PARAM_TOPICS));

            } else if (MyMQTTCommand.ACTION_PUBLISH.equals(action)) {
                onPublish(requestId, getParameter(intent, PARAM_TOPIC), intent.getByteArrayExtra(PARAM_PAYLOAD));

            } else if (MyMQTTCommand.ACTION_CHECK_CONNECTION.equals(action)) {
                broadcastConnectionStatus(requestId);
            }
        } catch (Throwable exc) {
            MQTTServiceLogger.error(getClass().getSimpleName(), "Error while processing command", exc);
        }
    }

    private int getInt(String string) {
        try {
            return Integer.parseInt(string, 10);
        } catch (Throwable exc) {
            MQTTServiceLogger.error(getClass().getSimpleName(), "Unparsable string: " + string + ", returning 0");
            return 0;
        }
    }

    private boolean onConnect(final String requestId,
                              final String brokerUrl,
                              final String clientId,
                              final String username,
                              final String password) {

        MQTTServiceLogger.debug(getClass().getSimpleName(), requestId + " Connect to "
                + brokerUrl + " with user: " + username + " and password: " + password);

        mConnectionRequestId = requestId;

        try {
            if (mClient == null) {
                MQTTServiceLogger.debug("onConnect", "Creating new MQTT connection");

                mTopicsToAutoResubscribe.clear();
                mClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
                mClient.setCallback(this);

                MqttConnectOptions connectOptions = new MqttConnectOptions();
                if (username != null && password != null) {
                    connectOptions.setUserName(username);
                    connectOptions.setPassword(password.toCharArray());
                }
                connectOptions.setCleanSession(true);
                connectOptions.setAutomaticReconnect(true);
                connectOptions.setKeepAliveInterval(KEEP_ALIVE_INTERVAL);
                connectOptions.setConnectionTimeout(CONNECT_TIMEOUT);

                mClient.connect(connectOptions);
                MQTTServiceLogger.debug("onConnect", "Connected");

            } else {
                reconnect(requestId);
            }

            return true;

        } catch (Exception exc) {
            broadcastException(BROADCAST_EXCEPTION, requestId, new MqttException(exc));
            return false;
        }
    }

    private void reconnect(String requestId) throws MqttException {
        if (mClient == null)
            return;

        if (mClient.isConnected()) {
            MQTTServiceLogger.debug("reconnect", "Client already connected, nothing to do");
        } else {
            MQTTServiceLogger.debug("reconnect", "Reconnecting MQTT");

            mClient.reconnect();
        }
    }

    private boolean clientIsConnected() {
        return (mClient != null && mClient.isConnected());
    }

    private void onDisconnect(final String requestId) {
        if (!clientIsConnected()) {
            MQTTServiceLogger.info("onDisconnect", "No client connected, nothing to disconnect!");
            return;
        }

        try {
            MQTTServiceLogger.debug("onDisconnect", "Disconnecting MQTT");
            mClient.disconnect();

        } catch (Exception e) {
            MQTTServiceLogger.error("onDisconnect",
                    "Error while disconnecting from MQTT. Request Id: " + requestId, e);

            try {
                mClient.disconnectForcibly();
            } catch (Exception exc) {
                MQTTServiceLogger.error("onDisconnect", "Error while disconnect forcibly", exc);
            }

        } finally {
            mClient = null;
            mTopicsToAutoResubscribe.clear();
            mShutdown = true;
        }
    }

    private void onSubscribe(final String requestId, final int qos,
                             final boolean autoResubscribeOnConnect,
                             final String... topics) {
        if (topics == null || topics.length == 0) {
            broadcastException(BROADCAST_SUBSCRIPTION_ERROR, requestId,
                    new Exception("No topics passed to subscribe!"),
                    PARAM_TOPIC, ""
            );
            return;
        }

        if (!clientIsConnected()) {
            for (String topic : topics) {
                broadcastException(BROADCAST_SUBSCRIPTION_ERROR, requestId,
                        new Exception("Can't subscribe to topics, client not connected!"),
                        PARAM_TOPIC, topic
                );
            }
            return;
        }

        for (String topic : topics) {
            try {
                MQTTServiceLogger.debug("onSubscribe", "Subscribing to topic: " + topic + " with QoS " + qos);
                mClient.subscribe(topic, qos);

                if (autoResubscribeOnConnect) {
                    mTopicsToAutoResubscribe.put(topic, qos);
                }

                MQTTServiceLogger.debug("onSubscribe", "Successfully subscribed to topic: " + topic);

                broadcast(BROADCAST_SUBSCRIPTION_SUCCESS, requestId,
                        PARAM_TOPIC, topic
                );
            } catch (Exception e) {
                broadcastException(BROADCAST_SUBSCRIPTION_ERROR, requestId, new MqttException(e),
                        PARAM_TOPIC, topic
                );
            }
        }
    }

    private void onPublish(final String requestId, final String topic, final byte[] payload) {
        if (!clientIsConnected()) {
            broadcastException(BROADCAST_EXCEPTION, requestId,
                    new Exception("Can't publish to topic: " + topic + ", client not connected!"));
            return;
        }

        try {
            MQTTServiceLogger.debug("onPublish", "Publishing to topic: " + topic + ", payload with size " + payload.length);
            MqttMessage message = new MqttMessage(payload);
            message.setQos(0);
            mClient.publish(topic, message);
            MQTTServiceLogger.debug("onPublish", "Successfully published to topic: " + topic + ", payload: " + payload);

            broadcast(BROADCAST_PUBLISH_SUCCESS, requestId,
                    PARAM_TOPIC, topic
            );

        } catch (Exception exc) {
            broadcastException(BROADCAST_EXCEPTION, requestId, new MqttException(exc));
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        MyUtils.Loge(TAG,"serverURI:"+serverURI);
        String requestId = reconnect ? UUID.randomUUID().toString() : mConnectionRequestId;

        if (reconnect) {
            MQTTServiceLogger.debug("reconnect", "Reconnected to " + serverURI);

            if (!mTopicsToAutoResubscribe.isEmpty()) {
                MQTTServiceLogger.debug("reconnect", "auto resubscribing to topics");
                for (Map.Entry<String, Integer> entry : mTopicsToAutoResubscribe.entrySet()) {
                    onSubscribe(requestId, entry.getValue(), true, entry.getKey());
                }
            }
        }

        broadcastConnectionStatus(requestId);
        broadcast(MyMQTTCommand.BROADCAST_CONNECTION_SUCCESS, requestId);

    }

    @Override
    public void connectionLost(Throwable cause) {
        broadcastConnectionStatus(UUID.randomUUID().toString());
        broadcastException(BROADCAST_EXCEPTION, UUID.randomUUID().toString(), new Exception(cause));
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        broadcastPayload(BROADCAST_MESSAGE_ARRIVED, UUID.randomUUID().toString(),
                mqttMessage.getPayload(), topic);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }


}
