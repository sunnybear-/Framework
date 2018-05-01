package com.sunnybear.framework.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;

/**
 * <p>
 * Created by chenkai.gu on 2018/4/23.
 */
public abstract class BaseBottomSheetDialog<VBD extends ViewDataBinding> extends BottomSheetDialog {

    protected VBD mViewDataBinding;

    public BaseBottomSheetDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public BaseBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
        initView(context);
    }

    protected BaseBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    /**
     * 设置布局id
     *
     * @return 布局id
     */
    public abstract int getLayoutId();

    /**
     * 绑定数据
     */
    public abstract void bindingData();

    private void initView(Context context) {
        mViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(), null, false);
        bindingData();
        setContentView(mViewDataBinding.getRoot());
    }
}
