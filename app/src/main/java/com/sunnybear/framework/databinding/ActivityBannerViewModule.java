package com.sunnybear.framework.databinding;

import android.arch.lifecycle.LifecycleOwner;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sunnybear.framework.R;
import com.sunnybear.framework.library.base.BaseViewModule;
import com.sunnybear.framework.module.BannerActivity;
import com.sunnybear.framework.ui.BadgeTabItem;
import com.sunnybear.framework.ui.viewpager.banner.BannerConfig;
import com.sunnybear.framework.ui.viewpager.banner.Image;
import com.sunnybear.framework.ui.viewpager.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/24.
 */
public class ActivityBannerViewModule extends BaseViewModule<BannerActivity, ActivityBannerBinding> {

    private ArrayList<BadgeTabItem> mBadgeTabItems;

    public ActivityBannerViewModule(BannerActivity bannerActivity, ActivityBannerBinding viewDataBinding) {
        super(bannerActivity, viewDataBinding);
    }

    @Override
    public void init() {
        List<Image> images = new ArrayList<>();
        Image image = new Image("1", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1554524880,2491058233&fm=27&gp=0.jpg");
        images.add(image);
        image = new Image("2", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527749868&di=63e58f60c2e71fad50a54f5367610ac3&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F43a7d933c895d143261d87d679f082025baf07df.jpg");
        images.add(image);
        image = new Image("3", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527155167871&di=5ad47f8903fa097bfc546bc1ccac03d8&imgtype=0&src=http%3A%2F%2Ff.expoon.com%2Fnews%2F2016%2F08%2F31%2F926774.jpg");
        images.add(image);
        image = new Image("4", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527155196076&di=60d2ed1a34f0ee19f4bd6c87ac10fcd9&imgtype=0&src=http%3A%2F%2Fpic35.photophoto.cn%2F20150617%2F0033033950635031_b.jpg");
        images.add(image);

        mViewDataBinding.banner.setOffscreenPageLimit(images.size());
        mViewDataBinding.banner.setImages(images)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setBannerAnimation(Transformer.MeiZu).start();

        ((ViewGroup) mViewDataBinding.banner.getParent()).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewDataBinding.banner.dispatchTouchEvent(event);
            }
        });

        mBadgeTabItems = new ArrayList<>();
        mBadgeTabItems.add(new BadgeTabItem(R.layout.tab_notification, "9+", true, true));
        mBadgeTabItems.add(new BadgeTabItem(R.layout.tab_activity, "9+", false, true));
        mBadgeTabItems.add(new BadgeTabItem(R.layout.tab_interactive, "9+", false, true));
        mBadgeTabItems.add(new BadgeTabItem(R.layout.tab_letters, "9+", false, true));
        mViewDataBinding.setBadgeTabItems(mBadgeTabItems);
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        mViewDataBinding.banner.startAutoPlay();
    }

    @Override
    public void onStop(LifecycleOwner owner) {
        mViewDataBinding.banner.stopAutoPlay();
    }
}
