package com.sunnybear.framework.ui.recyclerview;

/**
 * RecyclerView滚动事件监听
 * Created by chenkai.gu on 2018/3/22.
 */
public interface OnRecyclerViewScrollListener {

    void onScrollTop(int offset);

    void onScrollBottom();
}
