package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.utils.KeyUtils;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.SaveUtils;

import net.igenius.mqttservice.MQTTServiceCommand;

public class IninMQttDataActivity extends BaseActivity {

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideLoadingAnim(IninMQttDataActivity.this);
            MainActivity.start(IninMQttDataActivity.this);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inin_mqtt_data);
        initMQTT();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5*1000);
                    Message message=handler.obtainMessage();
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, IninMQttDataActivity.class);
        context.startActivity(intent);
    }

    private void initMQTT() {
        showLoadingAnim(this);
        if (!TextUtils.isEmpty(SaveUtils.getString(KeyUtils.MQTT_URL))) {
            MyUtils.Loge(TAG,"clientId："+ Build.SERIAL);
            MQTTServiceCommand.connect(getApplicationContext(),
                    "tcp://"+SaveUtils.getString(KeyUtils.MQTT_URL),
                    Build.SERIAL,
                    "admin",
                    "123456");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoadingAnim(this);
    }
}
