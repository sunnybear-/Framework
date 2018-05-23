package com.sunnybear.framework.library.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

/**
 * 基础DialogFragment
 * Created by chenkai.gu on 2018/3/27.
 */
public abstract class BaseDialogFragment<VDB extends ViewDataBinding> extends RxAppCompatDialogFragment {

    private View mFragmentView = null;

    protected VDB mViewDataBinding;

    private Bundle mArgs;

    protected Context mContext;

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();//接收Activity传递的参数
        //是dialog充满屏幕宽度
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mFragmentView = mViewDataBinding.getRoot();
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null)
            parent.removeView(mFragmentView);
        onBundle(mArgs);
        return mFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onStart() {
        super.onStart();
//        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
//        params.width = (int) (DensityUtil.getScreenW(getContext()) * 0.9f);
//        params.height = DensityUtil.getScreenH(getContext()) / 2;
//        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFragmentView != null)
            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
    }

    /**
     * 设置布局id
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 接收bundle传递参数
     *
     * @param args bundle参数
     */
    protected void onBundle(Bundle args) {

    }
}
