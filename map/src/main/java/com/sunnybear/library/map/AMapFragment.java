package com.sunnybear.library.map;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 高德地图Fragment
 * Created by chenkai.gu on 2018/3/23.
 */
@Route(path = Constant.ROUTER_MAP, group = Constant.GROUP)
public class AMapFragment extends SupportMapFragment
        implements AMap.OnMyLocationChangeListener, AMap.OnMarkerClickListener, AMap.OnMapLoadedListener {

    private static final String TAG = AMapFragment.class.getSimpleName();

    //定位间隔时间
    public static final String KEY_LOCATION_INTERVAL = "key_location_interval";
    private long mLocationInterval;
    //定位icon
    public static final String KEY_DRAWABLE_LOCATION = "key_drawable_location";
    private int mLocationIconRes = -1;
    //初始地图缩放等级
    public static final String KEY_ZOOM_LEVEL = "key_zoom_level";
    private int mZoomLevel;
    //标尺开关
    public static final String KEY_SCALE_CONTROLS = "key_scale_controls";
    private boolean isScaleControls;
    //指南针开关
    public static final String KEY_COMPASS = "key_compass";
    private boolean isCompass;
    //缩放按钮
    public static final String KEY_ZOOM_CONTROLS = "key_zoom_controls";
    private boolean isZoomControls;
    //缩放手势
    public static final String KEY_ZOOM_GESTURES = "key_zoom_gestures";
    private boolean isZoomGestures;
    //定位按钮
    public static final String KEY_LOCATION_BUTTON = "key_location_button";
    private boolean isLocationButton;
    //倾斜手势
    public static final String KEY_TILT_GESTURES = "key_tilt_gestures";
    private boolean isTiltGestures;
    //旋转手势
    public static final String KEY_ROTATE_GESTURES = "key_rotate_gestures";
    private boolean isRotateGestures;
    //滑动手势
    public static final String KEY_SCROLL_GESTURES = "key_scroll_gestures";
    private boolean isScrollGestures;
    //地图类型
    public static final String KEY_MAP_TYPE = "key_map_type";
    private int mMapType;
    //是否初始化时开启定位
    public static final String KEY_INIT_LOCATION = "key_init_location";
    private boolean isInitLocation;
    //MyLocationStyle
    public static final String KEY_MY_LOCATION_STYLE = "key_my_location_style";
    private int mMyLocationStyle;

    private View mFragmentView;

    private MapView mMapView;
    private AMap mAMap;

    private OnMapCallback mOnMapCallback;

    //marker标记点
    private List<Marker> mMarkers;

    public void setOnMapCallback(OnMapCallback onMapCallback) {
        mOnMapCallback = onMapCallback;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        if (args != null) {
            mLocationInterval = args.getLong(KEY_LOCATION_INTERVAL, Constant.LOCATION_INTERVAL);//定位间隔时间
            mLocationIconRes = args.getInt(KEY_DRAWABLE_LOCATION);//定位icon
            mZoomLevel = args.getInt(KEY_ZOOM_LEVEL, Constant.ZOOM);//初始缩放等级
            isScaleControls = args.getBoolean(KEY_SCALE_CONTROLS, false);//标尺开关
            isCompass = args.getBoolean(KEY_COMPASS, false);//指南针开关
            isZoomControls = args.getBoolean(KEY_ZOOM_CONTROLS, false); //缩放按钮
            isLocationButton = args.getBoolean(KEY_LOCATION_BUTTON, false); //定位按钮
            isZoomGestures = args.getBoolean(KEY_ZOOM_GESTURES, true);//缩放手势
            isTiltGestures = args.getBoolean(KEY_TILT_GESTURES, false);//倾斜手势
            isRotateGestures = args.getBoolean(KEY_ROTATE_GESTURES, false);//旋转手势
            isScrollGestures = args.getBoolean(KEY_SCROLL_GESTURES, true);//滑动手势
            mMapType = args.getInt(KEY_MAP_TYPE, AMap.MAP_TYPE_NORMAL);//地图类型
            isInitLocation = args.getBoolean(KEY_INIT_LOCATION, true);//是否初始化时开启定位
            mMyLocationStyle = args.getInt(KEY_MY_LOCATION_STYLE, MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//MyLocationStyle
        }
        mMarkers = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_map, container, false);
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null)
            parent.removeView(mFragmentView);
        //获取MapView对象
        mMapView = mFragmentView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        initMap();
        return mFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInitLocation)
            startLocation();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
        if (mFragmentView != null)
            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocation();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mMapView.onSaveInstanceState(savedInstanceState);
    }

    /**
     * 启动定位
     */
    public void startLocation() {
//        if (!isStartLocation()) {
        //设置为true表示启动显示定位蓝点,false表示隐藏定位蓝点并不进行定位,默认是false。
        mAMap.setMyLocationEnabled(true);
        mAMap.setMyLocationStyle(getMyLocationStyle());
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(mZoomLevel));
//        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (isStartLocation())
            //设置为true表示启动显示定位蓝点,false表示隐藏定位蓝点并不进行定位,默认是false。
            mAMap.setMyLocationEnabled(false);
    }


    /**
     * 是否开启定位
     *
     * @return
     */
    private boolean isStartLocation() {
        if (mAMap != null)
            return mAMap.isMyLocationEnabled();
        return false;
    }

    /**
     * 初始化map
     */
    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            initUiSetting();
            mAMap.setMapType(mMapType);//设置地图种类
            mAMap.setOnMarkerClickListener(this);
            mAMap.setOnMyLocationChangeListener(this);
            mAMap.setOnMapLoadedListener(this);
        }
    }

    /**
     * 初始化我的定位
     */
    private void initUiSetting() {
//        startLocation();
        UiSettings settings = mAMap.getUiSettings();
        settings.setLogoPosition(AMapOptions.LOGO_MARGIN_LEFT);//logo位置
        settings.setScaleControlsEnabled(isScaleControls);//标尺开关
        settings.setCompassEnabled(isCompass);//指南针开关
        settings.setZoomControlsEnabled(isZoomControls);//缩放按钮
        settings.setMyLocationButtonEnabled(isLocationButton);//定位按钮
        settings.setZoomGesturesEnabled(isZoomGestures);//缩放手势
        settings.setTiltGesturesEnabled(isTiltGestures);//倾斜手势
        settings.setRotateGesturesEnabled(isRotateGestures);//旋转手势
        settings.setScrollGesturesEnabled(isScrollGestures);//滑动手势
    }

    /**
     * 设置自定义定位蓝点
     */
    public MyLocationStyle getMyLocationStyle() {
        //初始化定位蓝点样式类
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔,只在连续定位模式下生效,单次定位模式下不会生效.单位为毫秒.
        myLocationStyle.interval(mLocationInterval);
        myLocationStyle.strokeColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的边框颜色的方法
        //myLocationStyle.strokeWidth(5);//设置定位蓝点精度圈的边框宽度的方法
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的填充颜色的方法
        //连续定位、且将视角移动到地图中心点,定位点依照设备方向旋转,并且会跟随设备移动.(1秒1次定位)如果不设置myLocationType,默认也会执行此种模式.
        myLocationStyle.myLocationType(mMyLocationStyle);
        //设置是否显示定位小蓝点,用于满足只想使用定位,不想使用定位小蓝点的场景,设置false以后图面上不再有定位蓝点的概念,但是会持续回调位置信息.
        myLocationStyle.showMyLocation(true);
        if (mLocationIconRes != -1)
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(mLocationIconRes));
        return myLocationStyle;
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();
            if (latitude != 0.0 && longitude != 0.0)
                MapHelper.regeoCode(getActivity(), latitude, longitude,
                        new GeocodeSearch.OnGeocodeSearchListener() {
                            @Override
                            public void onRegeocodeSearched(RegeocodeResult result, int code) {
                                if (code == AMapException.CODE_AMAP_SUCCESS && mOnMapCallback != null)
                                    mOnMapCallback.onMyLocation(latitude, longitude, result.getRegeocodeAddress());
                            }

                            @Override
                            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                            }
                        });
        } else {
            Log.e(TAG, "定位失败");
        }
    }

    /**
     * 添加marker标签
     *
     * @param latLng
     * @param markerIcon
     * @return
     */
    public void addMarkerOptions(LatLng latLng, String title, int markerIcon) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        if (!TextUtils.isEmpty(title))
            markerOptions.title(title);
        if (markerIcon != -1)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(markerIcon));
        //设置marker平贴地图效果
        markerOptions.setFlat(true);

        Marker marker = mAMap.addMarker(markerOptions);

//        Animation animation = new RotateAnimation(marker.getRotateAngle(), marker.getRotateAngle() + 180, 0, 0, 0);
//        long duration = 1000L;
//        animation.setDuration(duration);
//        animation.setInterpolator(new LinearInterpolator());
//
//        marker.setAnimation(animation);
//        marker.startAnimation();

        mMarkers.add(marker);
    }

    /**
     * 添加marker标签
     *
     * @param latLng
     * @return
     */
    public void addMarkerOptions(LatLng latLng) {
        addMarkerOptions(latLng, null, -1);
    }

    /**
     * 添加marker标签
     *
     * @param latLng
     * @return
     */
    public void addMarkerOptions(LatLng latLng, int markerIcon) {
        addMarkerOptions(latLng, null, markerIcon);
    }

    /**
     * 清除marker标记点
     */
    public void clearMarker() {
        if (mAMap != null && mMarkers.size() > 0) {
            for (Marker marker : mMarkers) {
                marker.remove();
            }
            mMarkers.clear();
            mMapView.invalidate();//刷新地图
        }
    }

    /**
     * 设置InfoWindowAdapter
     *
     * @param adapter
     */
    public void setInfoWindowAdapter(AMap.InfoWindowAdapter adapter) {
        if (mAMap != null)
            mAMap.setInfoWindowAdapter(adapter);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mOnMapCallback != null)
            mOnMapCallback.onMarkerClick(marker);
        return true;
    }

    /**
     * 移动地图至坐标点位置
     *
     * @param location 坐标经纬度
     */
    public void moveMap(LatLng location) {
        if (mAMap != null)
            mAMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    /**
     * 移动地图至坐标点位置
     *
     * @param location 坐标经纬度
     */
    public void moveMap(LatLng location, int zoomLevel) {
        if (mAMap != null)
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }

    /**
     * 是否显示路况
     *
     * @param isShow
     */
    public void showTrafficEnabled(boolean isShow) {
        if (mAMap != null)
            mAMap.setTrafficEnabled(isShow);
    }

    @Override
    public void onMapLoaded() {
        if (mOnMapCallback != null)
            mOnMapCallback.onMapLoadComplete();
    }

    /**
     * 地图回调
     */
    public interface OnMapCallback {

        /**
         * 当前我的位置回调
         */
        void onMyLocation(double latitude, double longitude, RegeocodeAddress address);

        /**
         * Marker标记点点击回调
         *
         * @param marker 点击的标记点
         */
        void onMarkerClick(Marker marker);

        /**
         * 地图加载完成回调
         */
        void onMapLoadComplete();
    }
}
