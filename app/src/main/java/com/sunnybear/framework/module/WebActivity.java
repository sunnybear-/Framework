package com.sunnybear.framework.module;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sunnybear.framework.R;
import com.sunnybear.framework.databinding.ActivityWebBinding;
import com.sunnybear.framework.databinding.ActivityWebViewModule;
import com.sunnybear.framework.library.base.BaseActivity;
import com.sunnybear.library.webkit.Constant;
import com.sunnybear.library.webkit.WebKitFragment;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/7.
 */
public class WebActivity extends BaseActivity<ActivityWebBinding, ActivityWebViewModule> {

    private WebKitFragment mWebKitFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected ActivityWebViewModule bindingViewModule(ActivityWebBinding viewDataBinding) {
        return new ActivityWebViewModule(this, viewDataBinding);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("网页");
        mViewModule.init();

        mWebKitFragment = (WebKitFragment) ARouter.getInstance()
                .build(Constant.ROUTER_WEB, Constant.GROUP)
                .navigation();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_web, mWebKitFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri result = data.getData();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWebKitFragment.load("file:///android_asset/index.html");
//        mWebKitFragment.load("http://www.youku.com");
    }

    @Override
    public void onBackPressed() {
        if (mWebKitFragment.canGoBack())
            mWebKitFragment.goBack();
        else
            super.onBackPressed();
    }
}
