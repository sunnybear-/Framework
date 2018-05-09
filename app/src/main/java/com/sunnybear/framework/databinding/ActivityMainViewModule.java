package com.sunnybear.framework.databinding;

import android.net.Uri;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sunnybear.framework.R;
import com.sunnybear.framework.library.base.BaseViewModule;
import com.sunnybear.framework.module.MainActivity;

/**
 * <p>
 * Created by chenkai.gu on 2018/4/29.
 */
public class ActivityMainViewModule extends BaseViewModule<MainActivity, ActivityMainBinding>
        implements View.OnClickListener {

    public ActivityMainViewModule(MainActivity mainActivity, ActivityMainBinding viewDataBinding) {
        super(mainActivity, viewDataBinding);
    }

    @Override
    public void init() {
        mViewDataBinding.setVm(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map:
//                StartHelper.with(mContext).startActivity(MapActivity.class);
//                StartHelper.with(mContext).startActivity(UrlReceiveActivity.class,
//                        "arouter://sunnybear/framework/map" +
//                                "?name=alex&age=18&boy=true&high=180&obj=%7b%22name%22%3a%22jack%22%2c%22id%22%3a666%7d");
                ARouter.getInstance()
                        .build(Uri.parse("arouter://sunnybear/framework/map" +
                                "?name=alex&age=18&boy=true&high=180&obj=%7b%22name%22%3a%22jack%22%2c%22id%22%3a666%7d"))
                        .navigation();
                break;
        }
    }
}
