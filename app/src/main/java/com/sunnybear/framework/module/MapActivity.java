package com.sunnybear.framework.module;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.sunnybear.framework.R;
import com.sunnybear.framework.databinding.ActivityMapBinding;
import com.sunnybear.framework.databinding.ActivityMapViewModule;
import com.sunnybear.framework.library.base.BaseActivity;
import com.sunnybear.framework.tools.StartHelper;
import com.sunnybear.framework.tools.log.Logger;
import com.sunnybear.library.map.AMapFragment;
import com.sunnybear.library.map.Constant;

import java.util.List;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/9.
 */
@Route(path = "/framework/map")
public class MapActivity extends BaseActivity<ActivityMapBinding, ActivityMapViewModule> {

    private AMapFragment mAMapFragment;

    public AMapFragment getAMapFragment() {
        return mAMapFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected ActivityMapViewModule bindingViewModule(ActivityMapBinding viewDataBinding) {
        return new ActivityMapViewModule(this, viewDataBinding);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("地图");
        mAMapFragment = (AMapFragment) ARouter.getInstance()
                .build(Constant.ROUTER_MAP, Constant.GROUP)
                .withInt(AMapFragment.KEY_DRAWABLE_LOCATION, R.mipmap.icon_location2)
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
            public void onMyLocation(double latitude, double longitude, RegeocodeAddress address) {
                List<RegeocodeRoad> roads = address.getRoads();
                for (RegeocodeRoad road : roads) {
                    Logger.i(road.getName());
                }
                List<Marker> markers = mAMapFragment.getScreenMarkers();
                Logger.i(markers);
            }

            @Override
            public void onMarkerClick(Marker marker) {
                Logger.i(marker);
                StartHelper.with(mContext)
                        .startActivity(WebActivity.class);
            }

            @Override
            public void onMapLoadComplete() {
                mAMapFragment.showTrafficEnabled(true);
            }
        });
        mViewModule.init();
    }
}
