package com.sunnybear.framework.ui.recyclerview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sunnybear.framework.library.base.Presenter;

import java.util.List;
import java.util.Map;

/**
 * 封装ViewHolder
 * Created by chenkai.gu on 2018/1/16.
 */
public abstract class BaseViewHolder<Item, VDB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected Context mContext;
    protected VDB mItemViewDataBinding;
    //item根布局
    protected View mItemView;

    private List<View> mBindViews;
    private Map<String, Object> mTagMap;

    protected Presenter mPresenter;

    final void setBindViews(List<View> bindViews) {
        mBindViews = bindViews;
    }

    final void setTagMap(Map<String, Object> tagMap) {
        mTagMap = tagMap;
    }

    public BaseViewHolder(VDB itemViewDataBinding) {
        this(itemViewDataBinding, null);
    }

    public BaseViewHolder(VDB itemViewDataBinding, Presenter presenter) {
        super(itemViewDataBinding.getRoot());
        mPresenter = presenter;
        mItemViewDataBinding = itemViewDataBinding;
        mItemView = mItemViewDataBinding.getRoot();

        if (presenter instanceof FragmentActivity)
            mContext = (Context) presenter;
        else if (presenter instanceof Fragment)
            mContext = ((Fragment) presenter).getContext();
    }

    public VDB getViewDataBinding() {
        return mItemViewDataBinding;
    }

    public void setViewDataBinding(VDB itemViewDataBinding) {
        mItemViewDataBinding = itemViewDataBinding;
    }

    /**
     * 绑定数据
     *
     * @param item
     */
    protected abstract void bindingData(Item item, int position);

    /**
     * 是否已经绑定tag
     *
     * @param item item
     */
    private boolean hasBindingTag(View view, Item item) {
        return mTagMap.containsKey(view.getId() + "_" + item.hashCode());
    }

    /**
     * 绑定tag
     *
     * @param view 目标View
     * @param item item
     */
    public final void bindingTag(View view, Item item, Object value) {
        if (!mBindViews.contains(view)) mBindViews.add(view);
        mTagMap.put(view.getId() + "_" + item.hashCode(), value);
    }

    /**
     * 获取绑定tag中的值
     *
     * @param view     目标View
     * @param item     item
     * @param defValue 默认值
     */
    public final <T> T getTagValue(View view, Item item, T defValue) {
        if (hasBindingTag(view, item))
            return (T) mTagMap.get(view.getId() + "_" + item.hashCode());
        return defValue;
    }
}
