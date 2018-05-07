package com.sunnybear.framework.databinding;

import com.sunnybear.framework.library.base.BaseViewModule;
import com.sunnybear.framework.module.WebActivity;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/7.
 */
public class ActivityWebViewModule extends BaseViewModule<WebActivity, ActivityWebBinding> {

    public ActivityWebViewModule(WebActivity webActivity, ActivityWebBinding viewDataBinding) {
        super(webActivity, viewDataBinding);
    }

    @Override
    public void init() {

    }
}
