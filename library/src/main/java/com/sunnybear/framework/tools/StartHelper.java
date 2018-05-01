package com.sunnybear.framework.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Activity启动助手
 * Created by chenkai.gu on 2018/4/12.
 */
public final class StartHelper {

    private Context mContext;
    private Bundle mBundle;

    private StartHelper(Context context) {
        mContext = context;
        mBundle = new Bundle();
    }

    public static StartHelper with(Context context) {
        return new StartHelper(context);
    }

    /**
     * 添加跳转参数
     *
     * @param key
     * @param value
     * @return
     */
    public StartHelper extra(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    /**
     * 添加跳转参数
     *
     * @param key
     * @param value
     * @return
     */
    public StartHelper extra(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    /**
     * 添加跳转参数
     *
     * @param key
     * @param value
     * @return
     */
    public StartHelper extra(String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    /**
     * 添加跳转参数
     *
     * @param key
     * @param value
     * @return
     */
    public StartHelper extra(String key, Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    /**
     * 添加跳转参数
     *
     * @param key
     * @param value
     * @return
     */
    public StartHelper extra(String key, Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

    /**
     * 启动Activity
     *
     * @param activityClass
     */
    public void startActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(mContext, activityClass);
        intent.putExtras(mBundle);
        mContext.startActivity(intent);
    }

    /**
     * 启动Activity带返回值
     *
     * @param activityClass
     */
    public void startActivityForResult(Class<? extends Activity> activityClass, int requestCode) {
        Intent intent = new Intent(mContext, activityClass);
        intent.putExtras(mBundle);
        if (mContext instanceof Activity)
            ((Activity) mContext).startActivityForResult(intent, requestCode);
        else
            throw new RuntimeException("mContext不是Activity类型");
    }

    /**
     * 启动Activity
     *
     * @param action
     */
    public void startActivity(String action) {
        Intent intent = new Intent(action);
        intent.putExtras(mBundle);
        mContext.startActivity(intent);
    }

    /**
     * 启动Activity带返回值
     *
     * @param action
     */
    public void startActivityForResult(String action, int requestCode) {
        Intent intent = new Intent(action);
        intent.putExtras(mBundle);
        if (mContext instanceof Activity)
            ((Activity) mContext).startActivityForResult(intent, requestCode);
        else
            throw new RuntimeException("mContext不是Activity类型");
    }
}
