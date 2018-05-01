package com.sunnybear.framework.ui.popup;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/21.
 */
public abstract class BasePopupWindow<VBD extends ViewDataBinding> extends EasyPopup {

    protected Context mContext;

    protected VBD mViewDataBinding;

    public BasePopupWindow(Context context) {
        super(context);
        mContext = context;
    }

    protected abstract int getLayoutId();

    @Override
    public void onPopupWindowCreated() {
        super.onPopupWindowCreated();
        //执行设置PopupWindow属性也可以通过Builder中设置
        //setContentView(x,x,x);
        //...
        mViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), getLayoutId(), null, false);
        setContentView(mViewDataBinding.getRoot());
        initAttributes();
    }

    @Override
    public void onPopupWindowViewCreated(View contentView) {
        initViews(contentView);
    }

    @Override
    public void onPopupWindowDismiss() {

    }

    /**
     * 可以在此方法中设置PopupWindow需要的属性
     */
    protected abstract void initAttributes();

    /**
     * 初始化view {@see getView()}
     *
     * @param view
     */
    protected abstract void initViews(View view);
}
