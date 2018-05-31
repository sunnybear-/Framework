package com.sunnybear.framework.ui;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.sunnybear.framework.library.glide.GlideRequests;
import com.sunnybear.framework.library.glide.ImageLoader;
import com.sunnybear.framework.tools.log.Logger;
import com.sunnybear.framework.ui.recyclerview.QuickRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 控件绑定适配器
 * Created by chenkai.gu on 2018/4/4.
 */
public final class WidgetBindingAdapter {

    /**
     * 转换HTML绑定到TextView
     *
     * @param textView
     * @param html
     */
    @BindingAdapter(value = {"android:html"})
    public static void htmlToText(TextView textView, String html) {
        if (!TextUtils.isEmpty(html))
            textView.setText(Html.fromHtml(html));
    }

    /**
     * 加载网络图片
     *
     * @param url            图片网络地址
     * @param placeholder    加载占位图
     * @param error          加载错误图
     * @param transformation 转换器
     */
    @BindingAdapter(value = {
            "android:imageUrl",
            "android:placeholder",
            "android:error",
            "android:transformation"
    }, requireAll = false)
    public static void imageLoader(ImageView imageView, String url, int placeholder, int error, Transformation<Bitmap> transformation) {
        GlideRequests requests = ImageLoader.with(imageView.getContext());
        if (requests != null && !TextUtils.isEmpty(url)) {
            RequestBuilder builder = requests.load(url)
                    .placeholder(placeholder).error(error)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Logger.d("Glide", "图片加载失败");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Logger.d("Glide", "图片加载成功");
                            return false;
                        }
                    });
            if (transformation != null)
                builder = builder.apply(RequestOptions.bitmapTransform(transformation));
            builder.transition(DrawableTransitionOptions.withCrossFade(500)).into(imageView);
        }
    }

    /**
     * BottomNavigationBar的栏位设置
     *
     * @param bottomNavigationBar
     * @param items
     */
    @BindingAdapter(value = {"android:bottomNavigationItems"})
    public static void setBottomNavigationItems(BottomNavigationBar bottomNavigationBar, List<BottomNavigationItem> items) {
        for (BottomNavigationItem item : items) {
            bottomNavigationBar.addItem(item);
        }
        bottomNavigationBar.setFirstSelectedPosition(0).initialise();
    }

    /**
     * 设置TabLayout的Item项目
     *
     * @param tabLayout
     * @param badgeItem     badgeTabItems的variable值(BR.XXXX)
     * @param badgeTabItems
     */
    @BindingAdapter(value = {"android:badgeItem", "android:badgeTabItems"})
    public static void setTabItem(TabLayout tabLayout, int badgeItem, final ArrayList<BadgeTabItem> badgeTabItems) {
        final ArrayList<ViewDataBinding> viewDataBindings = new ArrayList<>();
        if (badgeTabItems != null) {
            int size = badgeTabItems.size();
            for (int i = 0; i < size; i++) {
                BadgeTabItem badgeTabItem = badgeTabItems.get(i);
                ViewDataBinding viewDataBinding = DataBindingUtil
                        .inflate(LayoutInflater.from(tabLayout.getContext()), badgeTabItem.getBadgeItemLayoutId(),
                                null, false);
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setCustomView(viewDataBinding.getRoot());
                viewDataBinding.setVariable(badgeItem, badgeTabItem);
                viewDataBindings.add(viewDataBinding);
                tabLayout.addTab(tab);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                BadgeTabItem badgeTabItem = badgeTabItems.get(tab.getPosition());
                badgeTabItem.setSelect(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                BadgeTabItem badgeTabItem = badgeTabItems.get(tab.getPosition());
                badgeTabItem.setSelect(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @BindingAdapter(value = {"android:itemDecoration"})
    public static void addRecyclerViewItemDecoration(QuickRecyclerView quickRecyclerView, RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecoration != null)
            quickRecyclerView.addItemDecoration(itemDecoration);
    }
}
