package com.sunnybear.framework;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.model.Marker;
import com.sunnybear.framework.databinding.ActivityMainBinding;
import com.sunnybear.framework.library.base.BaseActivity;
import com.sunnybear.framework.tools.StartHelper;
import com.sunnybear.framework.tools.log.Logger;
import com.sunnybear.library.map.AMapFragment;
import com.sunnybear.library.map.Constant;

public class MainActivity extends BaseActivity<ActivityMainBinding, ActivityMainViewModule> {

    private AMapFragment mAMapFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected ActivityMainViewModule bindingViewModule(ActivityMainBinding viewDataBinding) {
        return new ActivityMainViewModule(this, viewDataBinding);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModule.init();

        mAMapFragment = (AMapFragment) ARouter.getInstance()
                .build(Constant.ROUTER_MAP)
                .withBoolean(AMapFragment.KEY_LOCATION_BUTTON, true)
                .withBoolean(AMapFragment.KEY_COMPASS, true)
                .withBoolean(AMapFragment.KEY_SCALE_CONTROLS, true)
                .withBoolean(AMapFragment.KEY_ZOOM_CONTROLS, true)
//                .withInt(AMapFragment.KEY_MAP_TYPE, AMap.MAP_TYPE_SATELLITE)
                .navigation();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_map, mAMapFragment).commit();

        mAMapFragment.setOnMapCallback(new AMapFragment.OnMapCallback() {
            @Override
            public void onLocation(Location location) {
                Logger.i(location.toString());
            }

            @Override
            public void onMarkerClick(Marker marker) {
                Logger.i(marker);
                StartHelper.with(mContext)
                        .startActivity(LocationActivity.class);
            }

            @Override
            public void onMapLoadComplete() {
                mAMapFragment.showTrafficEnabled(true);
            }
        });
    }
}
