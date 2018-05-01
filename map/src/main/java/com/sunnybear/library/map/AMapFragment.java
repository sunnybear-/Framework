package com.sunnybear.library.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 高德地图Fragment
 * Created by chenkai.gu on 2018/3/23.
 */
@Route(path = Constant.ROUTER_MAP)
public class AMapFragment extends SupportMapFragment
        implements AMapLocationListener, LocationSource, AMap.OnMarkerClickListener {

    //定位icon
    public static final String KEY_DRAWABLE_LOCATION = "key_drawable_location";
    private int mLocationIconRes = -1;
    //初始缩放等级
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

    private View mFragmentView;

    private MapView mMapView;
    private AMap mAMap;

    private OnMapCallback mOnMapCallback;

    //定位服务类。此类提供单次定位、持续定位、地理围栏、最后位置相关功能
    private AMapLocationClient mAMapLocationClient;
    private OnLocationChangedListener mOnLocationChangedListener;
    //定位参数设置
    private AMapLocationClientOption mAMapLocationClientOption;
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
            mLocationIconRes = args.getInt(KEY_DRAWABLE_LOCATION);//定位icon
            mZoomLevel = args.getInt(KEY_ZOOM_LEVEL, Constant.ZOOM);//初始缩放等级
            isScaleControls = args.getBoolean(KEY_SCALE_CONTROLS, false);//标尺开关
            isCompass = args.getBoolean(KEY_COMPASS, false);//指南针开关
            isZoomControls = args.getBoolean(KEY_ZOOM_CONTROLS, false); //缩放按钮
            isLocationButton = args.getBoolean(KEY_LOCATION_BUTTON, false); //定位按钮
            isZoomGestures = args.getBoolean(KEY_ZOOM_GESTURES, true);//缩放手势
            isTiltGestures = args.getBoolean(KEY_TILT_GESTURES, false);//倾斜手势
            isRotateGestures = args.getBoolean(KEY_ROTATE_GESTURES, true);//旋转手势
            isScrollGestures = args.getBoolean(KEY_SCROLL_GESTURES, true);//滑动手势
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
    public void onStart() {
        super.onStart();
        if (mAMapLocationClient != null)
            mAMapLocationClient.startLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        if (mAMapLocationClient != null)
            mAMapLocationClient.stopLocation();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFragmentView != null)
            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
        mMapView.onDestroy();
        //销毁定位客户端
        if (mAMapLocationClient != null) {
            mAMapLocationClient.onDestroy();
            mAMapLocationClient = null;
            mAMapLocationClientOption = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mMapView.onSaveInstanceState(savedInstanceState);
    }

    /**
     * 初始化map
     */
    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            initMyLocation();
            initLocationClient();
            mAMap.setOnMarkerClickListener(this);
        }
    }

    private void initLocationClient() {
        mAMapLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        mAMapLocationClient.setLocationListener(this);
        //初始化定位参数
        mAMapLocationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式,Battery_Saving为低功耗模式,Device_Sensors是仅设备模式
        mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mAMapLocationClientOption.setNeedAddress(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mAMapLocationClientOption.setInterval(Constant.LOCATION_INTERVAL);
        //给定位客户端对象设置定位参数
        mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
    }

    /**
     * 初始化我的定位
     */
    private void initMyLocation() {
        //设置定位监听
        mAMap.setLocationSource(this);
        //设置为true表示系统定位按钮显示并响应点击,false表示隐藏,默认是false
        mAMap.setMyLocationEnabled(true);
        mAMap.setMyLocationStyle(getMyLocationStyle());
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(mZoomLevel));
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
        myLocationStyle.strokeColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的边框颜色的方法
//        myLocationStyle.strokeWidth(5);//设置定位蓝点精度圈的边框宽度的方法
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的填充颜色的方法
        //连续定位、且将视角移动到地图中心点,定位点依照设备方向旋转,并且会跟随设备移动.(1秒1次定位)如果不设置myLocationType,默认也会执行此种模式.
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //设置是否显示定位小蓝点,用于满足只想使用定位,不想使用定位小蓝点的场景,设置false以后图面上不再有定位蓝点的概念,但是会持续回调位置信息.
        myLocationStyle.showMyLocation(true);
        if (mLocationIconRes != -1)
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(mLocationIconRes));
        return myLocationStyle;
    }

    /**
     * 添加marker标签
     *
     * @param latLng
     * @param position
     * @param markerIcon
     * @return
     */
    public void addMarkerOptions(LatLng latLng, int position, int markerIcon) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        if (position != -1)
            markerOptions.title(String.valueOf(position));
        if (markerIcon != -1)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(markerIcon));
        //设置marker平贴地图效果
        markerOptions.setFlat(true);
        mMarkers.add(mAMap.addMarker(markerOptions));
    }

    /**
     * 添加marker标签
     *
     * @param latLng
     * @return
     */
    public void addMarkerOptions(LatLng latLng) {
        addMarkerOptions(latLng, -1, -1);
    }

    /**
     * 添加marker标签
     *
     * @param latLng
     * @param markerIcon
     * @return
     */
    public void addMarkerOptions(LatLng latLng, int markerIcon) {
        addMarkerOptions(latLng, -1, markerIcon);
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        String title = marker.getTitle();
        if (mOnMapCallback != null && TextUtils.isDigitsOnly(title))
            mOnMapCallback.onMarkerClick(Integer.parseInt(title));
        return true;
    }

    /**
     * 移动地图至坐标点位置
     *
     * @param location 坐标经纬度
     */
    public void moveMap(LatLng location) {
        if (mAMap != null)
//            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constant.ZOOM));
            mAMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mOnLocationChangedListener != null && aMapLocation != null)
            if (aMapLocation.getErrorCode() == 0) {
                mOnLocationChangedListener.onLocationChanged(aMapLocation);//显示系统小蓝点
                if (mOnMapCallback != null)
                    mOnMapCallback.onLocation(aMapLocation);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AMapFragment", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mOnLocationChangedListener = null;
        if (mAMapLocationClient != null) {
            mAMapLocationClient.stopLocation();
            mAMapLocationClient.onDestroy();
        }
        mAMapLocationClient = null;
    }

    /**
     * 地图回调
     */
    public interface OnMapCallback {

        /**
         * 当前位置回调
         *
         * @param location
         */
        void onLocation(AMapLocation location);

        /**
         * Marker标记点点击回调
         *
         * @param position 点击项目的位置
         */
        void onMarkerClick(int position);
    }
}
