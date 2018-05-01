package com.sunnybear.framework.adapter;

import android.databinding.ViewDataBinding;

import com.sunnybear.framework.R;
import com.sunnybear.framework.databinding.ItemRecyclerBinding;
import com.sunnybear.framework.ui.recyclerview.BaseAdapter;

import java.util.List;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/1.
 */
public class RecyclerAdapter extends BaseAdapter<String, RecyclerViewHolder> {

    public RecyclerAdapter(List<String> strings) {
        super(strings);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_recycler;
    }

    @Override
    protected RecyclerViewHolder getViewHolder(int viewType, ViewDataBinding viewDataBinding) {
        return new RecyclerViewHolder((ItemRecyclerBinding) viewDataBinding);
    }
}
