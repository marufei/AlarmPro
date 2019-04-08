package com.huikezk.alarmpro.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.huikezk.alarmpro.R;


/**
 * Created by MaRufei on 2017/9/2.
 */

/**
 * @author: MaRufei
 * @date: 2017/11/20.
 * @Email: 867814102@qq.com
 * @Phone: 132 1358 0912
 * TODO:中间弹窗
 */


public class LoadingView extends Dialog {
    private String TAG = "LoadingView";
    private Context context;
    private String url;
    private ImageView dialog_ad_pic;

    public LoadingView(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_loading);
        //设置点击布局外则Dialog消失
        setCanceledOnTouchOutside(false);
    }


    public void showDialog() {
        Window window = getWindow();
        //设置弹窗动画
        window.setWindowAnimations(R.style.style_dialog);
        //设置Dialog背景色
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wl = window.getAttributes();
        //设置弹窗位置
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }
}
