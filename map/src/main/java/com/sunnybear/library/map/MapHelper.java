package com.sunnybear.library.map;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

/**
 * 地图工具
 * Created by chenkai.gu on 2018/3/23.
 */
public final class MapHelper {

    private static String POI_SEARCH_TYPE = "汽车服务|汽车销售|" +
            "//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
            "//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
            "//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

    /**
     * 一次定位工具
     *
     * @param context              context
     * @param aMapLocationListener 定位回调
     */
    public static void location(Context context, AMapLocationListener aMapLocationListener) {
        AMapLocationClient client = new AMapLocationClient(context);
        client.setLocationListener(aMapLocationListener);

        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果
        option.setOnceLocation(true);
        client.setLocationOption(option);
        //设置场景模式后最好调用一次stop,再调用start以保证场景模式生效
        client.stopLocation();
        client.startLocation();
    }

    /**
     * 导航工具类
     *
     * @param startLocation 起点位置
     * @param start         起点名称
     * @param endLocation   终点位置
     * @param end           终点名称
     * @param type          导航种类
     * @param callback      导航回调
     */
    public static void navigation(Context context, LatLng startLocation, String start, LatLng endLocation, String end,
                                  AmapNaviType type, INaviInfoCallback callback) {
        Poi startPoi = new Poi(start, startLocation, "");
        Poi endPoi = new Poi(end, endLocation, "");
        AmapNaviPage.getInstance().showRouteActivity(context,
                new AmapNaviParams(startPoi, null, endPoi, type), callback);
    }

    /**
     * 逆地理编码
     *
     * @param context
     * @param latitude
     * @param longitude
     * @param onGeocodeSearchListener
     */
    public static void geoCode(Context context, double latitude, double longitude, GeocodeSearch.OnGeocodeSearchListener onGeocodeSearchListener) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        if (onGeocodeSearchListener != null)
            geocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latitude, longitude), 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }

    /**
     * 开始异步POI搜索
     *
     * @param location            搜索中心点
     * @param keyword             关键字
     * @param distance            搜索范围
     * @param onPoiSearchListener 搜索回调
     */
    public static void startPOISearchAsyn(Context context, LatLng location, String keyword, int distance,
                                          PoiSearch.OnPoiSearchListener onPoiSearchListener) {
        //初始化搜索器
        PoiSearch.Query query = new PoiSearch.Query(keyword, POI_SEARCH_TYPE, "");
        query.setPageSize(100);
        LatLonPoint point = new LatLonPoint(location.latitude, location.longitude);
        PoiSearch search = new PoiSearch(context, query);
        search.setOnPoiSearchListener(onPoiSearchListener);
        search.setBound(new PoiSearch.SearchBound(point, distance, true));
        search.searchPOIAsyn();//启动异步搜索
    }

    /**
     * 开始同步POI搜索
     *
     * @param location 搜索中心点
     * @param keyword  关键字
     * @param distance 搜索范围
     */
    public static PoiResult startPOISearch(Context context, LatLng location, String keyword, int distance) throws AMapException {
        //初始化搜索器
        PoiSearch.Query query = new PoiSearch.Query(keyword, POI_SEARCH_TYPE, "");
        query.setPageSize(100);
        LatLonPoint point = new LatLonPoint(location.latitude, location.longitude);
        PoiSearch search = new PoiSearch(context, query);
        search.setBound(new PoiSearch.SearchBound(point, distance));
        return search.searchPOI();//启动异步搜索
    }

    /**
     * 导航回调
     */
    public static class SampleINaviInfoCallback implements INaviInfoCallback {
        @Override
        public void onInitNaviFailure() {

        }

        @Override
        public void onGetNavigationText(String s) {

        }

        @Override
        public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

        }

        @Override
        public void onArriveDestination(boolean b) {

        }

        @Override
        public void onStartNavi(int i) {

        }

        @Override
        public void onCalculateRouteSuccess(int[] ints) {

        }

        @Override
        public void onCalculateRouteFailure(int i) {

        }

        @Override
        public void onStopSpeaking() {

        }

        @Override
        public void onReCalculateRoute(int i) {

        }

        @Override
        public void onExitPage(int i) {

        }
    }
}
