package com.sunnybear.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sunnybear.framework.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 下拉刷新组件
 * Created by chenkai.gu on 2018/1/18.
 */
public class PullToRefreshLayout extends PtrFrameLayout implements PtrHandler {
    /*刷新头部视图*/
    private PullToRefreshHeader mPullToRefreshHeader;
    /*刷新监听器*/
    private OnRefreshListener mOnRefreshListener;

    static String mRefreshPrepareText;
    static String mRefreshRecyclerText;
    static String mRefreshLoadingText;
    static String mRefreshCompleteText;

    public PullToRefreshLayout(Context context) {
        this(context, null, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initStyleable(context, attrs);
    }

    private void initStyleable(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshLayout);
        mRefreshPrepareText = array.getString(R.styleable.PullToRefreshLayout_refreshPrepareText);
        mRefreshPrepareText = !TextUtils.isEmpty(mRefreshPrepareText) ? mRefreshPrepareText :
                context.getResources().getString(R.string.pullToRefresh_prepare);

        mRefreshRecyclerText = array.getString(R.styleable.PullToRefreshLayout_refreshRecycleText);
        mRefreshRecyclerText = !TextUtils.isEmpty(mRefreshRecyclerText) ? mRefreshRecyclerText :
                context.getResources().getString(R.string.pullToRefresh_recycle);

        mRefreshLoadingText = array.getString(R.styleable.PullToRefreshLayout_refreshLoadingText);
        mRefreshLoadingText = !TextUtils.isEmpty(mRefreshLoadingText) ? mRefreshLoadingText :
                context.getResources().getString(R.string.pullToRefresh_loading);

        mRefreshCompleteText = array.getString(R.styleable.PullToRefreshLayout_refreshCompleteText);
        mRefreshCompleteText = !TextUtils.isEmpty(mRefreshCompleteText) ? mRefreshCompleteText :
                context.getResources().getString(R.string.pullToRefresh_complete);
        array.recycle();
    }

    /**
     * 设置刷新头部视图
     *
     * @param pullToRefreshHeader 刷新头部视图
     */
    public void setPullToRefreshHeader(PullToRefreshHeader pullToRefreshHeader) {
        mPullToRefreshHeader = pullToRefreshHeader;
        setHeaderView(mPullToRefreshHeader);
        addPtrUIHandler(mPullToRefreshHeader);
        setPtrHandler(this);
    }

    /**
     * 设置刷新监听器
     *
     * @param onRefreshListener 刷新监听器
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return mPullToRefreshHeader.checkCanDoRefresh(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (mOnRefreshListener != null)
            mOnRefreshListener.onRefresh();
    }

    /**
     * 下拉刷新回调
     */
    public interface OnRefreshListener {

        void onRefresh();
    }
}
