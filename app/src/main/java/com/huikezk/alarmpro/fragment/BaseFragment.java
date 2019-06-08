package com.huikezk.alarmpro.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Administrator on 2016-08-15.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    public String TAG = "BaseFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //setUserVisibleHint  adapter中的每个fragment切换的时候都会被调用，如果是切换到当前页，那么isVisibleToUser==true，否则为false
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            show();
        } else {
            hidden();
        }
        super.onHiddenChanged(hidden);
    }

    protected void show() {

    }

    protected void hidden() {

    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {

        unLazyLoad();
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    protected abstract void unLazyLoad();


    @Override
    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();

    }

}
