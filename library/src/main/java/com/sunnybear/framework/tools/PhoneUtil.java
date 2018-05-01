package com.sunnybear.framework.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sunnybear.framework.tools.log.Logger;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 手机组件调用工具类
 * Created by chenkai.gu on 2018/3/21.
 */
public final class PhoneUtil {

    /**
     * 获取设备Id
     *
     * @param context context
     * @return 设备Id
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取设备名称
     */
    public static String getDeviceName() {
        return new Build().MODEL;
    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public static String getDeviceBrand() {
        try {
            return android.os.Build.BRAND;// android系统版本号
        } catch (Exception e) {
            return "未知";
        }
    }

    /**
     * 获取手机系统版本号
     *
     * @return 手机系统版本号
     */
    public static int getVersion() {
        try {
            return android.os.Build.VERSION.SDK_INT;// android系统版本号
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 得到CPU核心数
     *
     * @return CPU核心数
     */
    public static int getCpuCoreCount() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName()))
                        return true;
                    return false;
                }
            });
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 判断手机设备是否为双卡
     */
    public static boolean isDoubleSIM(Context context) {
        try {
            String imeiSIM2 = getOperatorSIM(context, "getDeviceIdGemini", 1);
            return !StringUtils.isEmpty(imeiSIM2);
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取手机IMEI号
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        String IMEI = null;
        try {
            if (isDoubleSIM(context))
                IMEI = getOperatorSIM(context, "getDeviceIdGemini", 0);
            else
                IMEI = ((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
        }
        return IMEI;
    }

    /**
     * 读取SIM卡槽信息
     *
     * @param predictedMethodName 反射方法名
     * @param simId               SIM卡槽id
     */
    private static String getOperatorSIM(Context context, String predictedMethodName, int simId) throws GeminiMethodNotFoundException {
        String result = null;
        @SuppressLint("ServiceCast")
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Method getSIMId = telephonyClass.getMethod(predictedMethodName, new Class[]{int.class});
            Object phone = getSIMId.invoke(telephony, new Object[]{simId});
            if (phone != null) result = phone.toString();
        } catch (Exception e) {
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }
        return result;
    }

    /**
     *
     */
    private static class GeminiMethodNotFoundException extends Exception {

        public GeminiMethodNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * 获取手机Wifi的ip地址
     *
     * @param context context
     */
    public static String getLocalIpAddress(Context context) {
        String ipAddress = null;
        try {
            NetworkInfo networkInfo =
                    ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_MOBILE://当前使用2G/3G/4G网络
                        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                            NetworkInterface intf = en.nextElement();
                            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                                InetAddress inetAddress = enumIpAddr.nextElement();
                                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                                    ipAddress = inetAddress.getHostAddress();
                            }
                        }
                        break;
                    case ConnectivityManager.TYPE_WIFI://当前使用无线网络
                        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                        break;
                }
            } else {
                Logger.e("没有连接网络", new RuntimeException("没有连接网络"));
                ipAddress = null;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ipAddress ip地址
     */
    private static String intIP2StringIP(int ipAddress) {
        return (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF) + "." + (ipAddress >> 24 & 0xFF);
    }

    /**
     * 是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取是否存在虚拟导航栏(NavigationBar)
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) hasNavigationBar = resources.getBoolean(id);
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method method = systemPropertiesClass.getMethod("get", String.class);
            String navigationBarOverride = (String) method.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            switch (navigationBarOverride) {
                case "0":
                    hasNavigationBar = true;
                    break;
                case "1":
                    hasNavigationBar = false;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 获取虚拟导航栏的高度
     *
     * @param context context
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 是否显示虚拟导航栏
     *
     * @param decorView
     * @param show
     */
    public static void showNavigationBar(View decorView, boolean show) {
        try {
            if (show)
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            else
                // Set the IMMERSIVE flag.
                // Set the content to appear under the system bars so that the content
                // doesn't resize when the system bars hide and show.
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置透明状态栏
     *
     * @param activity
     * @param on
     */
    public static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on)
            winParams.flags |= bits;
        else
            winParams.flags &= ~bits;
        win.setAttributes(winParams);
    }
}
