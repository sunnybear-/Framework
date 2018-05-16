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
                ARouter.getInstance()
                        .build(Uri.parse("arouter://sunnybear/framework/map"))
//                        .greenChannel()
                        .navigation();
                break;
        }
    }
}
