package com.sunnybear.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunnybear.framework.databinding.FragmentRecyclerBinding;
import com.sunnybear.framework.library.base.BaseFragment;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/1.
 */
public class RecyclerFragment extends BaseFragment<FragmentRecyclerBinding, FragmentRecyclerViewModule> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected FragmentRecyclerViewModule bindingViewModule(FragmentRecyclerBinding viewDataBinding) {
        return new FragmentRecyclerViewModule(this, viewDataBinding);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModule.init();
    }
}
