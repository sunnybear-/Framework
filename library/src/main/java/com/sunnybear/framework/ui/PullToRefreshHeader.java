package com.sunnybear.framework.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sunnybear.framework.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * 刷新头部视图
 * Created by chenkai.gu on 2018/1/18.
 */
public abstract class PullToRefreshHeader extends FrameLayout implements PtrUIHandler {
    /*状态识别*/
    protected int mState;

    /*重置*/
    public static final int STATE_RESET = -1;
    /*准备刷新*/
    public static final int STATE_PREPARE = 0;
    /*开始刷新*/
    public static final int STATE_BEGIN = 1;
    /*结束刷新*/
    public static final int STATE_FINISH = 2;

    /*提醒文本*/
    protected TextView mRefreshText;

    public PullToRefreshHeader(@NonNull Context context) {
        this(context, null, 0);
    }

    public PullToRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        View headerView = getHeaderView(getContext(), this);
        mRefreshText = headerView.findViewById(R.id.refresh_text);
        addView(headerView);
    }

    /**
     * 设置HeaderView
     *
     * @param context context
     * @param root    PullToRefreshHeader
     */
    public abstract View getHeaderView(Context context, ViewGroup root);

    /**
     * 下拉刷新规则定义
     *
     * @param frame
     * @param content
     * @param header
     * @return
     */
    public abstract boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header);

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mState = STATE_RESET;
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mState = STATE_PREPARE;
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mState = STATE_BEGIN;
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mState = STATE_FINISH;
    }

    /**
     * 处理提醒字体
     *
     * @param frame
     * @param isUnderTouch
     * @param status
     * @param ptrIndicator
     */
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        switch (mState) {
            case STATE_PREPARE:
                if (ptrIndicator.getCurrentPercent() < 1.2)
                    mRefreshText.setText(PullToRefreshLayout.mRefreshPrepareText);
                else
                    mRefreshText.setText(PullToRefreshLayout.mRefreshRecyclerText);
                break;
            case STATE_BEGIN:
                mRefreshText.setText(PullToRefreshLayout.mRefreshLoadingText);
                break;
            case STATE_FINISH:
                mRefreshText.setText(PullToRefreshLayout.mRefreshCompleteText);
                break;
        }
    }
}
