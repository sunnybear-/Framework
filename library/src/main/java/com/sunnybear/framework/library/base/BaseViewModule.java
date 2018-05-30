package com.sunnybear.framework.library.base;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sunnybear.framework.library.base.annotation.BundleInject;

import java.lang.reflect.Field;

/**
 * 基础封装ViewModule
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
        ARouter.getInstance().inject(this);
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
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                BundleInject bundleInject = field.getAnnotation(BundleInject.class);
                if (bundleInject != null) {
                    String name = bundleInject.name();
                    field.setAccessible(true);
                    field.set(this, args.get(name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(LifecycleOwner owner) {

    }

    @Override
    public void onStart(LifecycleOwner owner) {

    }

    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {

    }

    @Override
    public void onStop(LifecycleOwner owner) {

    }

    @Override
    public void onDestroy(LifecycleOwner owner) {

    }
}
