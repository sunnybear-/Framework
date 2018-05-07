package com.sunnybear.framework.library.base;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.sunnybear.framework.tools.log.Logger;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/9.
 */
public abstract class BaseViewModule<T extends Presenter, VDB extends ViewDataBinding>
        implements ViewModule {

    protected Context mContext;
    /*主持人,类型为泛型T的类型*/
    protected T mPresenter;
    protected VDB mViewDataBinding;

    public BaseViewModule(T t, VDB viewDataBinding) {
        this.mPresenter = t;
        mViewDataBinding = viewDataBinding;

        if (t instanceof FragmentActivity)
            mContext = (Context) t;
        else if (t instanceof Fragment)
            mContext = ((Fragment) t).getContext();
    }

    /**
     * 初始化方法
     */
    public abstract void init();

    /**
     * 接受参数
     *
     * @param args
     */
    public void onBundle(Bundle args) {

    }

    @Override
    public void onCreate(LifecycleOwner owner) {
        Logger.w(getClass().getSimpleName() + "=====onCreate");
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        Logger.w(getClass().getSimpleName() + "=====onStart");
    }

    @Override
    public void onResume(LifecycleOwner owner) {
        Logger.w(getClass().getSimpleName() + "=====onResume");
    }

    @Override
    public void onPause(LifecycleOwner owner) {
        Logger.w(getClass().getSimpleName() + "=====onPause");
    }

    @Override
    public void onStop(LifecycleOwner owner) {
        Logger.w(getClass().getSimpleName() + "=====onStop");
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        Logger.w(getClass().getSimpleName() + "=====onDestroy");
    }
}
