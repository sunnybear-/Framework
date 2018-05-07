package com.sunnybear.framework.library.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Logger.d("BaseViewModule.onCreate:" + this.getClass().toString());
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Logger.d("BaseViewModule.onDestroy:" + this.getClass().toString());
    }

    @Override
    public void onLifecycleChanged(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event) {

    }

    /**
     * 接受参数
     *
     * @param args
     */
    public void onBundle(Bundle args) {

    }
}
