package com.sunnybear.framework.adapter;

import com.sunnybear.framework.databinding.ItemRecyclerBinding;
import com.sunnybear.framework.ui.recyclerview.BaseViewHolder;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/1.
 */
public class RecyclerViewHolder extends BaseViewHolder<String, ItemRecyclerBinding> {

    public RecyclerViewHolder(ItemRecyclerBinding itemViewDataBinding) {
        super(itemViewDataBinding);
    }

    @Override
    protected void bindingData(String s, int position) {
        mItemViewDataBinding.setData(s);
    }
}
