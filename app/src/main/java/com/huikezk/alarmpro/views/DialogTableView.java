package com.huikezk.alarmpro.views;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.huikezk.alarmpro.R;
import com.huikezk.alarmpro.utils.MyUtils;


/**
 * Created by MaRufei on 2017/9/2.
 */

/**
 * @author: MaRufei
 * @date: 2017/11/20.
 * @Email: 867814102@qq.com
 * @Phone: 132 1358 0912
 * TODO:底部弹窗
 */


public class DialogTableView extends Dialog {
    private String TAG = "DialogYearView";
    public OnEventClickListenner onEventClickListenner;
    private Context context;


    public DialogTableView(Context context, int layoutId) {
        super(context);
        this.context = context;
        setContentView(layoutId);
        //设置点击布局外则Dialog消失
        setCanceledOnTouchOutside(false);
    }

    public void setOnEventClickListenner(OnEventClickListenner onEventClickListenner) {
        this.onEventClickListenner = onEventClickListenner;
    }

    public void showDialog() {
        Window window = getWindow();

        //设置弹窗动画
        window.setWindowAnimations(R.style.style_dialog);
        //设置Dialog背景色
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wl = window.getAttributes();
        //定义宽度
        wl.width = window.getWindowManager().getDefaultDisplay().getWidth();
        //设置弹窗位置
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
        show();
        final EditText dialog_table_describe = findViewById(R.id.dialog_table_describe);
        final EditText dialog_table_deviceid=findViewById(R.id.dialog_table_deviceid);
        final EditText dialog_table_number=findViewById(R.id.dialog_table_number);

        findViewById(R.id.dialog_table_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.dialog_table_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(dialog_table_describe.getText().toString())){
                    MyUtils.showToast(context,"请输入电表描述");
                    return;
                }
                if (TextUtils.isEmpty(dialog_table_deviceid.getText().toString())){
                    MyUtils.showToast(context,"请输入电表号");
                    return;
                }
                if (TextUtils.isEmpty(dialog_table_number.getText().toString())){
                    MyUtils.showToast(context,"请输入参数个数");
                    return;
                }
                dismiss();
                onEventClickListenner.onSure(dialog_table_describe.getText().toString(),
                        dialog_table_deviceid.getText().toString(),
                        dialog_table_number.getText().toString());
            }
        });

    }

    public interface OnEventClickListenner {

        void onSure(String describe,String deviceid,String number);
    }
}
