package com.sunnybear.framework.module;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunnybear.framework.R;
import com.sunnybear.framework.databinding.ActivityBannerBinding;
import com.sunnybear.framework.databinding.ActivityBannerViewModule;
import com.sunnybear.framework.library.base.BaseActivity;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/24.
 */
public class BannerActivity extends BaseActivity<ActivityBannerBinding, ActivityBannerViewModule> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_banner;
    }

    @Override
    protected ActivityBannerViewModule bindingViewModule(ActivityBannerBinding viewDataBinding) {
        return new ActivityBannerViewModule(this, viewDataBinding);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("广告");
        mViewModule.init();
    }
}
