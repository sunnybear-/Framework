package com.sunnybear.framework.ui.recyclerview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.sunnybear.framework.library.base.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 封装RecyclerView的Adapter
 * Created by chenkai.gu on 2018/1/16.
 */
public abstract class BaseAdapter<Item, VH extends BaseViewHolder> extends DelegateAdapter.Adapter<VH> {

    protected Context mContext;
    private ViewDataBinding mViewDataBinding;

    protected List<Item> mItems;

    private List<View> mBindViews;
    private Map<String, Object> mTagMap;

    protected Presenter mPresenter;

    public BaseAdapter(List<Item> items) {
        this(items, null);
    }

    public BaseAdapter(List<Item> items, Presenter presenter) {
        mItems = items != null ? items : new ArrayList<Item>();
        mPresenter = presenter;

        mTagMap = new ConcurrentHashMap<>();
        mBindViews = new ArrayList<>();

        if (presenter instanceof FragmentActivity)
            mContext = (Context) presenter;
        else if (presenter instanceof Fragment)
            mContext = ((Fragment) presenter).getContext();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getItemLayoutId(viewType), parent, false);
        return getViewHolder(viewType, viewDataBinding);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        mViewDataBinding = holder.getViewDataBinding();
        holder.setBindViews(mBindViews);
        holder.setTagMap(mTagMap);
        holder.bindingData(mItems.get(position), position);
        //执行绑定
        mViewDataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public List<Item> getItems() {
        return mItems;
    }

    /**
     * 创建LayoutHelper
     *
     * @return 默认为线性布局
     */
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    /**
     * 设置布局id
     *
     * @return
     */
    protected abstract int getItemLayoutId(int viewType);

    /**
     * 设置ViewHolder
     *
     * @param viewDataBinding
     * @return
     */
    protected abstract VH getViewHolder(int viewType, ViewDataBinding viewDataBinding);

    /**
     * 获得item
     *
     * @param position 标号
     * @return item
     */
    public final Item getItem(int position) {
        return mItems.get(position);
    }

    /**
     * 添加
     *
     * @param item item
     */
    public final void add(Item item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    /**
     * 添加
     *
     * @param position 下标
     * @param item     item
     */
    public final void add(int position, Item item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 添加全部
     *
     * @param items items
     */
    public final void addAll(List<Item> items) {
        int position = 0;
        if (mItems.size() > 0)
            position = mItems.size() - 1;
        mItems.addAll(items);
        notifyItemRangeInserted(position, items.size());
        notifyItemRangeChanged(position + items.size(), getItemCount() - items.size());
    }

    /**
     * 替换
     *
     * @param oldItem 老item
     * @param newItem 新item
     */
    public final void replace(Item oldItem, Item newItem) {
        int index = mItems.indexOf(oldItem);
        replace(index, newItem);
    }

    /**
     * 替换
     *
     * @param index 下标
     * @param item  item
     */
    public final void replace(int index, Item item) {
        mItems.set(index, item);
        notifyItemChanged(index);
    }

    /**
     * 替换全部
     *
     * @param items items
     */
    public final void replaceAll(List<Item> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 删除
     *
     * @param item item
     */
    public final void delete(Item item) {
        for (View view : mBindViews) {
            if (mTagMap.containsKey(view.getId() + "_" + item.hashCode()))
                mTagMap.remove(view.getId() + "_" + item.hashCode());
        }
        mItems.remove(item);
        notifyItemRemoved(mItems.indexOf(item));
    }

    /**
     * 删除
     *
     * @param index item下标
     */
    public final void delete(int index) {
        delete(getItem(index));
    }

    /**
     * 清除
     */
    public final void clear() {
        mBindViews.clear();
        mTagMap.clear();

        mItems.clear();
        notifyDataSetChanged();
    }

    /**
     * 刷新
     */
    public final void refresh() {
        notifyDataSetChanged();
    }

    /**
     * 是否包含Item
     *
     * @param item item
     */
    public final boolean contains(Item item) {
        return mItems.contains(item);
    }
}
