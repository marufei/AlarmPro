package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.huikezk.alarmpro.adapter.RepairGvAdapter;
import com.huikezk.alarmpro.entity.UploadEntity;
import com.huikezk.alarmpro.service.IListener;
import com.huikezk.alarmpro.service.ListenerManager;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.MyUtils;
import com.huikezk.alarmpro.utils.PicassoUtlis;
import com.huikezk.alarmpro.utils.PictureUtil;
import com.huikezk.alarmpro.utils.UploadUtil;
import com.huikezk.alarmpro.utils.VolleyUtils;
import com.huikezk.alarmpro.views.GridViewForScrollView;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewRepairActivity extends BaseActivity implements View.OnClickListener, IListener {

    private String TAG = "NewRepairActivity";
    private EditText new_repair_content;
    private Button new_repair_commit;
    private LinearLayout new_repair_update;
    private GridViewForScrollView new_repair_gv;
    private int REQUEST_CODE = 0x001;
    private ArrayList<String> images = new ArrayList<>();
    private RepairGvAdapter adapter;
    private List<String> picNewList = new ArrayList<>();
    private int MSG_DOWN_SUCCESS = 1;
    private List<String> urlList=new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_DOWN_SUCCESS) {
                urlList.add(MyApplication.IP.substring(0,MyApplication.IP.length()-1)+msg.obj);
            }
            if (msg.what==MSG_ALL_SUCCESS){
                urlList.add(MyApplication.IP.substring(0,MyApplication.IP.length()-1)+msg.obj);
                adapter.setListData((ArrayList<String>) urlList);
                adapter.notifyDataSetChanged();
            }
        }
    };
    private int MSG_ALL_SUCCESS=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_repair);
        setBack(true);
        setTitle("新建报修");
        ToolBarStyle(1);
        initView();
        ListenerManager.getInstance().registerListtener(this);

    }

    private void initView() {
        new_repair_content = findViewById(R.id.new_repair_content);
        new_repair_commit = findViewById(R.id.new_repair_commit);
        new_repair_commit.setOnClickListener(this);
        new_repair_update = findViewById(R.id.new_repair_update);
        new_repair_update.setOnClickListener(this);
        new_repair_gv = findViewById(R.id.new_repair_gv);
        adapter = new RepairGvAdapter(this);
        new_repair_gv.setAdapter(adapter);
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, NewRepairActivity.class);
        context.startActivity(intent);
    }

    /**
     * 报修
     */
    public void repair() {
        showLoadingAnim(this);
        String url = MyApplication.IP + HttpsConts.REPAIR + MyApplication.PROJECT_NUM;
        MyUtils.Loge(TAG, "URL:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(NewRepairActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        finish();
                        MyUtils.showToast(NewRepairActivity.this, "提交成功");
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(NewRepairActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(NewRepairActivity.this);
                MyUtils.showToast(NewRepairActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                if (!TextUtils.isEmpty(MyApplication.NICK_NAME)) {
                    map.put("nickName", MyApplication.NICK_NAME);
                }
                if (!TextUtils.isEmpty(MyApplication.USER_NAME)) {
                    map.put("username", MyApplication.USER_NAME);
                }
                if (!TextUtils.isEmpty(new_repair_content.getText().toString())) {
                    map.put("repairInfo", new_repair_content.getText().toString().trim());
                }
                if (urlList.size() > 0) {
                    map.put("imgs", urlList.toString());
                    MyUtils.Loge(TAG, "imgs:" + urlList.toString());
                }
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(NewRepairActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_repair_commit:
                if (TextUtils.isEmpty(new_repair_content.getText().toString())) {
                    MyUtils.showToast(this, "请先输入具体报修内容");
                    return;
                }
                repair();
                break;
            case R.id.new_repair_update:
                //限数量的多选(比喻最多9张)
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .setMaxSelectCount(9) // 图片的最大选择数量，小于等于0时，不限数量。
                        .setSelected(images) // 把已选的图片传入默认选中。
                        .setViewImage(true) //是否点击放大图片查看,，默认为true
                        .start(this, REQUEST_CODE); // 打开相册
                break;
        }
    }


    /**
     * 上传图片
     *
     * @param file
     */
    private void update(final File file, final boolean isLast) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String url = MyApplication.IP + HttpsConts.UPDATE_FILE;
                MyUtils.Loge(TAG, "url:" + url);
                Map<String, File> files = new HashMap<String, File>();
                files.put("files", file);
                try {
                    String request = UploadUtil.post(url, null, files);
                    MyUtils.Loge(TAG, "request:" + request);
                    Gson gson = new Gson();
                    UploadEntity uploadEntity = gson.fromJson(request, UploadEntity.class);
                    if (uploadEntity != null && uploadEntity.getData() != null && uploadEntity.getData().size() > 0) {
                        Message msg = new Message();
                        if (isLast){
                            msg.what = MSG_ALL_SUCCESS;
                        }else {
                            msg.what = MSG_DOWN_SUCCESS;
                        }
                        msg.obj = uploadEntity.getData().get(0);
                        handler.sendMessage(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    MyUtils.Loge(TAG, "e:" + e.getMessage());
                }
            }
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            images.clear();
            //获取选择器返回的数据
            images = data.getStringArrayListExtra(
                    ImageSelectorUtils.SELECT_RESULT);

            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
            for (int i = 0; i < images.size(); i++) {
                MyUtils.Loge(TAG, "images.size():" + images.get(i));
//                picList.add(PictureUtil.imageToBase64(images.get(i)));
                picNewList.add(PictureUtil.compressImage(images.get(i), ".png"));
                if (i==images.size()-1) {
                    update(new File(images.get(i)),true);
                }else {
                    update(new File(images.get(i)),false);
                }
            }
        }
    }


}
