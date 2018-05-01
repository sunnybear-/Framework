package com.sunnybear.framework.ui.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.sunnybear.framework.R;
import com.sunnybear.framework.tools.log.Logger;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Arrays;
import java.util.List;

/**
 * 快速部署的RecyclerView
 * Created by chenkai.gu on 2018/1/17.
 */
public class QuickRecyclerView extends RecyclerView {
    private VirtualLayoutManager mVirtualLayoutManager;
    private DelegateAdapter mDelegateAdapter;
    /*当hasConsistItemType = true的时候,不论是不是属于同一个子adapter,相同类型的item都能复用.表示它们共享一个类型.
      当hasConsistItemType = false的时候,不同子adapter之间的类型不共享.*/
    private boolean hasConsistItemType;
    private boolean hasItemDecoration;//是否需要间隔线

    private int mItemDecorationMarginLeft;
    private int mItemDecorationMarginRight;
    private int mDividerColor;
    private int mDividerHeight;
    private boolean isNestedScrolling = false;//是否嵌套使用

    private int offset;

    //RecyclerView滚动事件监听
    private OnRecyclerViewScrollListener mOnRecyclerViewScrollListener;

    public void setOnRecyclerViewScrollListener(OnRecyclerViewScrollListener onRecyclerViewScrollListener) {
        mOnRecyclerViewScrollListener = onRecyclerViewScrollListener;
    }

    public QuickRecyclerView(Context context) {
        this(context, null, 0);
    }

    public QuickRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initStyleable(context, attrs);
        mVirtualLayoutManager = new VirtualLayoutManager(context);
        setLayoutManager(mVirtualLayoutManager);
        mDelegateAdapter = new DelegateAdapter(mVirtualLayoutManager, hasConsistItemType);

        setOverScrollMode(View.OVER_SCROLL_NEVER);

        //间隔线
        if (hasItemDecoration) {
            HorizontalDividerItemDecoration.Builder build = new HorizontalDividerItemDecoration.Builder(context)
                    .margin(mItemDecorationMarginLeft, mItemDecorationMarginRight)
                    .size(mDividerHeight);
            if (mDividerColor != 0)
                build.color(mDividerColor);
            addItemDecoration(build.build());
        }
        //滑动到底部监听
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Logger.d("QuickRecyclerView", "------->isSlideToBottom:" + isSlideToBottom(recyclerView));
                if (mOnRecyclerViewScrollListener != null) {
                    if (isSlideToBottom(recyclerView))
                        mOnRecyclerViewScrollListener.onScrollBottom();
                    else if (isSlideToTop())
                        mOnRecyclerViewScrollListener.onScrollTop(offset);
                }
            }
        });
        setHasFixedSize(true);
        //嵌套滑动
        setNestedScrollingEnabled(isNestedScrolling);
    }

    /**
     * 是否滑动到顶部
     *
     * @return
     */
    public boolean isSlideToTop() {
        offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (-1 < 0)
            return !(offset > 0);
        else
            return !(offset < range - 1);
    }

    /**
     * 是否滑动到底部
     *
     * @param recyclerView
     * @return
     */
    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >=
                recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void initStyleable(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QuickRecyclerView);
        hasConsistItemType = array.getBoolean(R.styleable.QuickRecyclerView_hasConsistItemType, false);
        hasItemDecoration = array.getBoolean(R.styleable.QuickRecyclerView_hasItemDecoration, true);
        mItemDecorationMarginLeft = array.getDimensionPixelSize(R.styleable.QuickRecyclerView_marginLeftItemDecoration, 0);
        mItemDecorationMarginRight = array.getDimensionPixelSize(R.styleable.QuickRecyclerView_marginRightItemDecoration, 0);
        mDividerColor = array.getColor(R.styleable.QuickRecyclerView_dividerColor, 0);
        mDividerHeight = array.getDimensionPixelSize(R.styleable.QuickRecyclerView_dividerHeight, 1);
        isNestedScrolling = array.getBoolean(R.styleable.QuickRecyclerView_nestedScrolling, false);
        array.recycle();
    }

    /**
     * 添加Adapter到列表上
     */
    public void setAdapter(DelegateAdapter.Adapter adapter) {
        mDelegateAdapter.addAdapter(adapter);
        super.setAdapter(mDelegateAdapter);
    }

    /**
     * 添加多个Adapter到列表上
     */
    public void setAdapters(List<DelegateAdapter.Adapter> adapters) {
        mDelegateAdapter.addAdapters(adapters);
        super.setAdapter(mDelegateAdapter);
    }

    /**
     * 添加多个Adapter到列表上
     */
    public void setAdapters(DelegateAdapter.Adapter... adapters) {
        List<DelegateAdapter.Adapter> list = Arrays.asList(adapters);
        setAdapters(list);
    }

    public DelegateAdapter getAdapter() {
        return mDelegateAdapter;
    }

    /**
     * 跳转到指定行
     *
     * @param position 行号
     * @param offset   偏移量
     */
    public void skipPosition(int position, int offset) {
        mVirtualLayoutManager.scrollToPositionWithOffset(position, offset);
    }

    /**
     * 跳转到指定行
     *
     * @param position 行号
     */
    public void skipPosition(int position) {
        skipPosition(position, 0);
    }

    /**
     * 跳转下一行
     *
     * @param offset 偏移量
     */
    public void skipNext(int offset) {
        View topView = mVirtualLayoutManager.getChildAt(0);
        int currentPosition = mVirtualLayoutManager.getPosition(topView);
        int next = currentPosition + 1;
        int size = getAdapter().getItemCount();
        if (next < size) skipPosition(next, offset);
    }

    /**
     * 跳转下一行
     */
    public void skipNext() {
        skipNext(0);
    }

    /**
     * 跳转上一行
     *
     * @param offset 偏移量
     */
    public void skipPrevious(int offset) {
        View topView = mVirtualLayoutManager.getChildAt(0);
        int currentPosition = mVirtualLayoutManager.getPosition(topView);
        int PreviousPosition = currentPosition - 1;
        if (PreviousPosition >= 0) skipPosition(PreviousPosition, offset);
    }

    /**
     * 跳转上一行
     */
    public void skipPrevious() {
        skipPrevious(0);
    }

    /**
     * 添加底边
     *
     * @param footLayoutId 底边布局
     */
    public void addFootView(final int footLayoutId) {
        getAdapter().addAdapter(new DelegateAdapter.Adapter() {
            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new SingleLayoutHelper();
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(getContext()).inflate(footLayoutId, parent, false)) {
                };
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnBottomClickListener != null)
                            mOnBottomClickListener.onBottomClick();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return 1;
            }
        });
    }

    private onBottomClickListener mOnBottomClickListener;

    public void setOnBottomClickListener(onBottomClickListener onBottomClickListener) {
        mOnBottomClickListener = onBottomClickListener;
    }

    public interface onBottomClickListener {

        void onBottomClick();
    }
}
