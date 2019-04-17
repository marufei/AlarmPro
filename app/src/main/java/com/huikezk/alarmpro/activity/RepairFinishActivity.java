package com.huikezk.alarmpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.huikezk.alarmpro.HttpsAddress.HttpsConts;
import com.huikezk.alarmpro.MyApplication;
import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.adapter.RepairGvAdapter;
import com.huikezk.alarmpro.entity.RepairEntity;
import com.huikezk.alarmpro.entity.UploadEntity;
import com.huikezk.alarmpro.utils.ActivityUtil;
import com.huikezk.alarmpro.utils.MyUtils;
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

public class RepairFinishActivity extends BaseActivity implements View.OnClickListener {

    private TextView repair_finish_name, repair_finish_time, repair_finish_content;
    private EditText repair_finish_et_input;
    private LinearLayout repair_finish_update;
    private Button repair_finish_commit;
    private RepairEntity repairEntity;
    private int REQUEST_CODE=0x002;
    private ArrayList<String> images=new ArrayList<>();
    private ArrayList<String> picList=new ArrayList<>();
    private GridViewForScrollView repair_finish_gv;
    private RepairGvAdapter adapter;
    private int MSG_ALL_SUCCESS=1;
    private int MSG_DOWN_SUCCESS=2;
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
                commit();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_finish);
        setBack(true);
        ToolBarStyle(1);
        setTitle("维修");
        initView();
        initData();

    }

    private void initData() {
        repairEntity = (RepairEntity) getIntent().getSerializableExtra("repairBean");
        if (repairEntity != null) {
            setView();
        }
    }

    private void initView() {
        repair_finish_name = findViewById(R.id.repair_finish_name);
        repair_finish_time = findViewById(R.id.repair_finish_time);
        repair_finish_content = findViewById(R.id.repair_finish_content);
        repair_finish_et_input = findViewById(R.id.repair_finish_et_input);
        repair_finish_update = findViewById(R.id.repair_finish_update);
        repair_finish_update.setOnClickListener(this);
        repair_finish_commit = findViewById(R.id.repair_finish_commit);
        repair_finish_commit.setOnClickListener(this);
        repair_finish_gv=findViewById(R.id.repair_finish_gv);
        adapter=new RepairGvAdapter(this);
        repair_finish_gv.setAdapter(adapter);
    }

    public static void start(Context context, RepairEntity listData) {
        Intent intent = new Intent();
        intent.putExtra("repairBean", listData);
        intent.setClass(context, RepairFinishActivity.class);
        context.startActivity(intent);
    }

    private void setView() {
        if (!TextUtils.isEmpty(repairEntity.getNickName())) {
            repair_finish_name.setText(repairEntity.getNickName());
        }
        if (!TextUtils.isEmpty(repairEntity.getDatetime())) {
            repair_finish_time.setText(repairEntity.getDatetime());
        }
        if (!TextUtils.isEmpty(repairEntity.getRepairInfo())) {
            repair_finish_content.setText(repairEntity.getRepairInfo());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_finish_commit:
                if (TextUtils.isEmpty(repairEntity.getId())) {
                    MyUtils.showToast(RepairFinishActivity.this, "数据异常");
                    return;
                }
                if (TextUtils.isEmpty(repair_finish_et_input.getText().toString())) {
                    MyUtils.showToast(RepairFinishActivity.this, "请先输入维修内容");
                    return;
                }
                showLoadingAnim(this);
                if (images.size()>0) {
                    for (int i = 0; i < images.size(); i++) {
                        MyUtils.Loge(TAG, "images.size():" + images.get(i));
                        if (i == images.size() - 1) {
                            update(new File(images.get(i)), true);
                        } else {
                            update(new File(images.get(i)), false);
                        }
                    }
                }else {
                    commit();
                }

                break;
            case R.id.repair_finish_update:
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
     * 提交维修信息
     */
    private void commit() {
        String url = MyApplication.IP + HttpsConts.REPAIR + MyApplication.PROJECT_NUM;
        MyUtils.Loge(TAG, "URL:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                hideLoadingAnim(RepairFinishActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        finish();
                        MyUtils.showToast(RepairFinishActivity.this, "提交成功");
                    } else {
                        String msg = jsonObject.getString("message");
                        ActivityUtil.toLogin(RepairFinishActivity.this, status, msg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoadingAnim(RepairFinishActivity.this);
                MyUtils.showToast(RepairFinishActivity.this, "网络有问题");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", repairEntity.getId());
                if (!TextUtils.isEmpty(repairEntity.getNickName())){
                    map.put("nickName", repairEntity.getNickName());
                }
                if (!TextUtils.isEmpty(repairEntity.getUsername())) {
                    map.put("username", repairEntity.getUsername());
                }
                if (!TextUtils.isEmpty(MyApplication.NICK_NAME)) {
                    map.put("finishNickName", MyApplication.NICK_NAME);
                }
                if (!TextUtils.isEmpty(MyApplication.USER_NAME)) {
                    map.put("finishUsername", MyApplication.USER_NAME);
                }
                map.put("finishRepairInfo", repair_finish_et_input.getText().toString().trim());
                if (urlList.size()>0) {
                    map.put("finishImgs", new Gson().toJson(urlList));
                }
                MyUtils.Loge(TAG,"nickName:"+repairEntity.getNickName());
                MyUtils.Loge(TAG,"userName:"+repairEntity.getUsername());
                MyUtils.Loge(TAG,"finishNickName:"+MyApplication.NICK_NAME);
                MyUtils.Loge(TAG,"finishUserName:"+MyApplication.USER_NAME);
                return map;
            }
        };
        VolleyUtils.setTimeOut(stringRequest);
        VolleyUtils.getInstance(RepairFinishActivity.this).addToRequestQueue(stringRequest);
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
            adapter.setListData(images);
            adapter.notifyDataSetChanged();

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
                    hideLoadingAnim(RepairFinishActivity.this);
                }
            }
        }.start();

    }
}
