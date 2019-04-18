package com.huikezk.alarmpro.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.activity.AlarmActivity;
import com.huikezk.alarmpro.activity.EditPwdActivity;
import com.huikezk.alarmpro.activity.LoginActivity;
import com.huikezk.alarmpro.activity.RepairHistoryActivity;
import com.huikezk.alarmpro.activity.WorkRankActivity;
import com.huikezk.alarmpro.entity.UploadEntity;
import com.huikezk.alarmpro.service.MyMqttService;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.PicassoUtlis;
import com.huikezk.alarmpro.utils.PictureUtil;
import com.huikezk.alarmpro.utils.SaveUtils;
import com.huikezk.alarmpro.utils.UploadUtil;
import com.huikezk.alarmpro.utils.VolleyUtils;
import com.umeng.message.UTrack;

import net.igenius.mqttservice.MQTTServiceCommand;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by MaRufei
 * on 2019/1/3.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose:TODO
 * update：
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private LinearLayout mine_change, mine_rank, mine_alarm, mine_repair;
    private Button mine_login_out;
    private TextView mine_name;
    private ImageView mine_pic;
    private static final int REQUEST_CODE = 0x03;
    private ArrayList<String> images = new ArrayList<>();
    private List<String> picList = new ArrayList<>();
    private String picStr;
    private String TAG = "MineFragment";

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            headerUrl=MyApplication.IP+msg.obj;
            if (msg.what==MSG_DOWN_SUCCESS){
                RequestOptions options = new RequestOptions()
                        .error(R.drawable.vector_drawable_circle)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transform(new CircleCrop());
                Glide.with(getActivity())
                        .load(MyApplication.IP+msg.obj)
                        .apply(options)
                        .transition(new DrawableTransitionOptions()
                                .crossFade())
                        .into(mine_pic);
                updateHeader(MyApplication.IP+msg.obj);
            }
        }
    };
    private int MSG_DOWN_SUCCESS=1;
    private String headerUrl;

    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = View.inflate(getActivity(), R.layout.fragment_mine, null);
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (!TextUtils.isEmpty(MyApplication.NICK_NAME)) {
            mine_name.setText(MyApplication.NICK_NAME);
        }
        if (!TextUtils.isEmpty(MyApplication.PIC_URL)) {
            RequestOptions options = new RequestOptions()
                    .error(R.drawable.vector_drawable_circle)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transform(new CircleCrop());
            Glide.with(getActivity())
                    .load(MyApplication.PIC_URL)
                    .apply(options)
                    .transition(new DrawableTransitionOptions()
                            .crossFade())
                    .into(mine_pic);
        }
    }

    private void initViews() {
        mine_name = view.findViewById(R.id.mine_name);
        mine_pic = view.findViewById(R.id.mine_pic);
        mine_pic.setOnClickListener(this);
        mine_change = view.findViewById(R.id.mine_change);
        mine_change.setOnClickListener(this);
        mine_rank = view.findViewById(R.id.mine_rank);
        mine_rank.setOnClickListener(this);
        mine_alarm = view.findViewById(R.id.mine_alarm);
        mine_alarm.setOnClickListener(this);
        mine_repair = view.findViewById(R.id.mine_repair);
        mine_repair.setOnClickListener(this);
        mine_login_out = view.findViewById(R.id.mine_login_out);
        mine_login_out.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_change:
                EditPwdActivity.start(getActivity());
                break;
            case R.id.mine_rank:
                WorkRankActivity.start(getActivity());
                break;
            case R.id.mine_alarm:
                AlarmActivity.start(getActivity());
                break;
            case R.id.mine_repair:
                RepairHistoryActivity.start(getActivity());
                break;
            case R.id.mine_login_out:
                loginOut();
                break;
            case R.id.mine_pic:
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(true)  //设置是否单选
                        .setMaxSelectCount(1) // 图片的最大选择数量，小于等于0时，不限数量。
                        .setViewImage(true) //是否点击放大图片查看,，默认为true
                        .start(this, REQUEST_CODE); // 打开相册
                break;
        }
    }

    /**
     * 退出
     */
    private void loginOut() {

        String url = HttpsConts.BASE_URL + HttpsConts.LOGIN_OUT;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        MyUtils.Loge("lbw", "===mqtt onDestroy");
                        MQTTServiceCommand.disconnect(getActivity().getApplicationContext());
                        getActivity().stopService(new Intent(getActivity().getApplicationContext(), MyMqttService.class));
                        SaveUtils.removeAllData();
                        MyApplication.finishAllActivity();
                        ActivityUtil.exitAll();
                        LoginActivity.start(getActivity());

                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(getActivity(), status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MyUtils.showToast(getActivity(), "网络有问题");
            }
        });
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    /**
     * 删除友盟别名
     */
    private void delUmengAlias() {
        //移除别名ID
        MyApplication.getInstance().getmPushAgent().deleteAlias("别名ID", "自定义类型", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            images.clear();
            //获取选择器返回的数据
            images.addAll(data.getStringArrayListExtra(
                    ImageSelectorUtils.SELECT_RESULT));

            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
            if (images != null && images.size() > 0) {
                picStr = PictureUtil.compressImage(images.get(0), ".png");
//                updateHeader();
                MyUtils.Loge(TAG,"files:"+images.get(0));
                MyUtils.Loge(TAG,"files:"+new File(images.get(0)));
                update(new File(images.get(0)),"file:///"+images.get(0));
            }


        }
    }

    /**
     * 上传头像
     */
    private void updateHeader(final String headerUrl) {
        String url = HttpsConts.BASE_URL + HttpsConts.UPDATE_HEADER;
        MyUtils.Loge(TAG, "URL:" + url);
        MyUtils.Loge(TAG, "picHeader:" + picStr);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        if (images!=null&&!TextUtils.isEmpty(images.get(0))) {
                            RequestOptions options = new RequestOptions()
                                    .error(R.drawable.vector_drawable_circle)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .transform(new CircleCrop());
                            Glide.with(getActivity())
                                    .load("file://" + images.get(0))
                                    .apply(options)
                                    .transition(new DrawableTransitionOptions()
                                            .crossFade())
                                    .into(mine_pic);
                        }

                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(getActivity(), status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.Loge(TAG, "error:" + error.getMessage());
                MyUtils.showToast(getActivity(), "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", MyApplication.USER_ID);
                map.put("picHeader", headerUrl);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    /**
     * 上传头像
     * @param file
     * @param uri
     */
    private void update(final File file, final String uri) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String url = MyApplication.IP + HttpsConts.UPDATE_FILE;
                MyUtils.Loge(TAG,"url:"+url);
                Map<String, File> files = new HashMap<String, File>();
                files.put("files", file);
                try {
                    String request = UploadUtil.post(url, null, files);
                    MyUtils.Loge(TAG, "request:" + request);
                    Gson gson=new Gson();
                    UploadEntity uploadEntity=gson.fromJson(request,UploadEntity.class);
                    if (uploadEntity!=null&&uploadEntity.getData()!=null&&uploadEntity.getData().size()>0){
                        Message msg = new Message();
                        msg.what = MSG_DOWN_SUCCESS;
                        msg.obj=uploadEntity.getData().get(0);
                        handler.sendMessage(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    MyUtils.Loge(TAG, "e:" + e.getMessage());
                }
            }
        }.start();

    }

}
