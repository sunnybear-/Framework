package com.sunnybear.framework.library.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import io.reactivex.Flowable;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/9.
 */
public abstract class BaseViewModule<T extends Presenter, VDB extends ViewDataBinding> {

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

    public T getPresenter() {
        return mPresenter;
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

    /**
     * 处理EventBus消息
     *
     * @param messageTag
     * @param messageBody
     */
    public void disposeEvent(String messageTag, Flowable<T> messageBody) {

    }
}
