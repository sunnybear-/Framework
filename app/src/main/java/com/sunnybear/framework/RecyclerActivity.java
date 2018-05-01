package com.sunnybear.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunnybear.framework.databinding.ActivityRecyclerBinding;
import com.sunnybear.framework.library.base.BaseActivity;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/1.
 */
public class RecyclerActivity extends BaseActivity<ActivityRecyclerBinding, ActivityRecyclerViewModule> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycler;
    }

    @Override
    protected ActivityRecyclerViewModule bindingViewModule(ActivityRecyclerBinding viewDataBinding) {
        return new ActivityRecyclerViewModule(this, viewDataBinding);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModule.init();
    }
}
