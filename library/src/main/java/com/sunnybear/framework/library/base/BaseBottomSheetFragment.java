package com.sunnybear.framework.library.base;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunnybear.framework.tools.log.Logger;

/**
 * 基础封装BottomSheetDialogFragment
 * Created by chenkai.gu on 2018/5/2.
 */
public abstract class BaseBottomSheetFragment<VDB extends ViewDataBinding> extends BottomSheetDialogFragment {

    protected Context mContext;

    protected VDB mViewDataBinding;

    private View mRootView;
    private BottomSheetBehavior mBehavior;

    private Dialog mDialog;

    private Bundle mArgs;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();//接收Activity传递的参数
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除缓存View和当前ViewGroup的关联
        ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
        if (viewGroup != null)
            viewGroup.removeView(mRootView);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //每次打开都调用该方法 类似于onCreateView 用于返回一个Dialog实例
        mDialog = super.onCreateDialog(savedInstanceState);
        if (mRootView == null) {
            //缓存下来的View 当为空时才需要初始化 并缓存
            mViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), getLayoutId(), null, false);
            mRootView = mViewDataBinding.getRoot();
            init();
        }
        onBundle(mArgs);
        /**
         * 设置显示的View到Dialog中
         * 抽象方法 子类可重写
         * 默认添加的View 高度为Wrap 某些场景需要固定高度
         * */
        mDialog.setContentView(mRootView);//设置View重新关联
        mBehavior = BottomSheetBehavior.from((View) mRootView.getParent());
        mBehavior.setHideable(true);
        //让父View背景透明是圆角边的关键
        ((View) mRootView.getParent()).setBackgroundColor(Color.TRANSPARENT);
        mRootView.post(new Runnable() {
            @Override
            public void run() {
                /**
                 * PeekHeight默认高度256dp会在该高度上悬浮
                 * 设置等于view的高就不会卡住
                 */
                mBehavior.setPeekHeight(mRootView.getHeight());
            }
        });
        resetView();
        return mDialog;
    }

    /**
     * 接收bundle传递参数
     *
     * @param args bundle参数
     */
    protected void onBundle(Bundle args) {

    }

    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 使用关闭弹框 是否使用动画可选
     * 使用动画 同时切换界面Aty会卡顿 建议直接关闭
     */
    public void dismiss(boolean isAnimation) {
        if (isAnimation) {
            if (mBehavior != null)
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            dismiss();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!this.isAdded())
            super.show(manager, tag);
        else
            Logger.d(this + " has add to FragmentManager");
    }

    /**
     * 设置布局id
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化View和设置数据等操作的方法
     */
    protected abstract void init();

    /**
     * 重置的View和数据的空方法 子类可以选择实现
     * 为避免多次inflate 父类缓存rootView
     * 所以不会每次打开都调用{@link #init()}方法
     * 但是每次都会调用该方法 给子类能够重置View和数据
     */
    public void resetView() {

    }
}
