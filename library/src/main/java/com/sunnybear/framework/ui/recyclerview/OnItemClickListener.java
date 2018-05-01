package com.sunnybear.framework.ui.recyclerview;

/**
 * RecyclerView的item点击事件监听器
 * Created by chenkai.gu on 2018/2/4.
 */
public interface OnItemClickListener<Item> {

    void onItemClick(Item item, int position);
}
