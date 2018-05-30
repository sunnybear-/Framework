package com.sunnybear.framework.library.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunnybear.framework.library.base.annotation.BundleInject;
import com.sunnybear.framework.library.eventbus.EventBusMessage;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.Field;

/**
 * 基础Fragment,主管模组分发
 * Created by chenkai.gu on 2018/1/14.
 */
public abstract class BaseFragment<VDB extends ViewDataBinding, VM extends BaseViewModule> extends RxFragment implements Presenter {

    private View mFragmentView = null;

    protected Context mContext;
    private VDB mViewDataBinding;

    protected VM mViewModule;

    private Bundle mArgs;

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();//接收Activity传递的参数
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mViewModule = bindingViewModule(mViewDataBinding);
        getLifecycle().addObserver(mViewModule);
        mFragmentView = mViewDataBinding.getRoot();
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null)
            parent.removeView(mFragmentView);
        onBundle(mArgs);
        return mFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除缓存View和当前ViewGroup的关联
        if (mFragmentView != null)
            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(mViewModule);
    }

    /**
     * 设置布局id
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 绑定ViewModule
     *
     * @param viewDataBinding
     * @return
     */
    protected abstract VM bindingViewModule(VDB viewDataBinding);

    /**
     * 接收bundle传递参数
     *
     * @param args bundle参数
     */
    void onBundle(Bundle args) {
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
        mViewModule.onBundle(args);
    }

    /**
     * EventBus接收方法
     *
     * @param message
     * @param <T>
     */
    @Deprecated
    public <T> void onSubscriberEvent(EventBusMessage<T> message) {

    }
}
