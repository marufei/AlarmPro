package com.huikezk.alarmpro.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.huikezk.alarmpro.R;


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


public class DialogYearView extends Dialog {
    private String TAG = "DialogYearView";
    public OnEventClickListenner onEventClickListenner;
    private Context context;
    private int [] array_year;


    public DialogYearView(Context context, int layoutId,int [] array_year) {
        super(context);
        this.context = context;
        this.array_year=array_year;
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
        wl.gravity = Gravity.BOTTOM;
        window.setAttributes(wl);
        show();
        final TextView tv_dialog_year1 = findViewById(R.id.tv_dialog_year1);
        tv_dialog_year1.setText(String.valueOf(array_year[0]));
        final TextView tv_dialog_year2 = findViewById(R.id.tv_dialog_year2);
        tv_dialog_year2.setText(String.valueOf(array_year[1]));
        final TextView tv_dialog_year3 = findViewById(R.id.tv_dialog_year3);
        tv_dialog_year3.setText(String.valueOf(array_year[2]));
        tv_dialog_year1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onEventClickListenner != null) {
                    tv_dialog_year1.setTextColor(getContext().getResources().getColor(R.color.blue_6a));
                    tv_dialog_year2.setTextColor(getContext().getResources().getColor(R.color.gray_4a));
                    tv_dialog_year3.setTextColor(getContext().getResources().getColor(R.color.gray_4a));

                    onEventClickListenner.onContent1();
                }
            }
        });
        tv_dialog_year2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onEventClickListenner != null) {
                    tv_dialog_year1.setTextColor(getContext().getResources().getColor(R.color.gray_4a));
                    tv_dialog_year2.setTextColor(getContext().getResources().getColor(R.color.blue_6a));
                    tv_dialog_year3.setTextColor(getContext().getResources().getColor(R.color.gray_4a));

                    onEventClickListenner.onContent2();
                }
            }
        });
        tv_dialog_year3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onEventClickListenner != null) {
                    tv_dialog_year1.setTextColor(getContext().getResources().getColor(R.color.gray_4a));
                    tv_dialog_year2.setTextColor(getContext().getResources().getColor(R.color.gray_4a));
                    tv_dialog_year3.setTextColor(getContext().getResources().getColor(R.color.blue_6a));


                    onEventClickListenner.onContent3();
                }
            }
        });
        findViewById(R.id.tv_dialog_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
    }

    public interface OnEventClickListenner {

        void onContent1();

        void onContent2();

        void onContent3();
    }
}
