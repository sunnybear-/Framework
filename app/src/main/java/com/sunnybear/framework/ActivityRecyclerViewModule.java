package com.sunnybear.framework;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.sunnybear.framework.databinding.ActivityRecyclerBinding;
import com.sunnybear.framework.library.base.BaseViewModule;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/1.
 */
public class ActivityRecyclerViewModule extends BaseViewModule<RecyclerActivity, ActivityRecyclerBinding> {

    TabLayout mTabLayout;

    public ActivityRecyclerViewModule(RecyclerActivity recyclerActivity, ActivityRecyclerBinding viewDataBinding) {
        super(recyclerActivity, viewDataBinding);
    }

    @Override
    public void init() {
        mTabLayout = mViewDataBinding.tab;
        mTabLayout.addTab(mTabLayout.newTab().setText("tab1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("tab2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("tab3"));

        mViewDataBinding.viewPager.setAdapter(new FragmentPagerAdapter(mPresenter.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return Fragment.instantiate(mContext, RecyclerFragment.class.getName());
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }
}
