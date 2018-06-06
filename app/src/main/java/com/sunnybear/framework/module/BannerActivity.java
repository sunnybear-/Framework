package com.sunnybear.framework.module;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sunnybear.framework.R;
import com.sunnybear.framework.databinding.ActivityBannerBinding;
import com.sunnybear.framework.databinding.ActivityBannerViewModule;
import com.sunnybear.framework.entity.User;
import com.sunnybear.framework.library.base.BaseActivity;
import com.sunnybear.framework.tools.log.Logger;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/24.
 */
@Route(path = "/framework/banner")
public class BannerActivity extends BaseActivity<ActivityBannerViewModule, ActivityBannerBinding> {

    @Autowired
    User mUser;

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
        ARouter.getInstance().inject(this);
        setTitle("广告");
        mViewModule.init();

        Logger.i(mUser.toString());
    }
}
