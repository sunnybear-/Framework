package com.sunnybear.framework.ui.recyclerview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;

/**
 * single适配器
 * Created by chenkai.gu on 2018/4/13.
 */
public abstract class SingleAdapter<VDB extends ViewDataBinding> extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;

    protected VDB mViewDataBinding;

    protected View mItemView;

    public SingleAdapter(Context context) {
        mContext = context;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new SingleLayoutHelper();
    }

    /**
     * 设置布局id
     *
     * @return
     */
    public abstract int getLayoutId();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                getLayoutId(), parent, false);
        if (mViewDataBinding == null)
            throw new NullPointerException("ViewDataBinding == null");
        mItemView = mViewDataBinding.getRoot();
        return new RecyclerView.ViewHolder(mItemView) {
        };
    }

    /**
     * 绑定数据
     *
     * @param holder
     */
    protected abstract void bindingHolder(RecyclerView.ViewHolder holder);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindingHolder(holder);
        mViewDataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
