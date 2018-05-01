package com.sunnybear.framework.tools;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.util.List;

/**
 * 偏好设置提供器
 * Created by chenkai.gu on 2017/3/14.
 */
public final class PreferenceHelper {

    /**
     * 初始化偏好设置(初始化可能需要36-400ms)
     *
     * @param context context
     */
    public static void initialize(Context context) {
        Hawk.init(context)
                .setEncryption(new NoEncryption()).build();
    }

    /**
     * 保存偏好设置数据
     *
     * @param key   key
     * @param value 数据
     * @param <T>   类型
     */
    public static <T> void insert(String key, T value) {
        Hawk.put(key, value);
    }

    /**
     * 保存偏好设置数据集合
     *
     * @param key    key
     * @param values 数据集合
     * @param <T>    类型
     */
    public static <T> void insert(String key, List<T> values) {
        Hawk.put(key, values);
    }


    /**
     * 获得偏好设置数据
     *
     * @param key      key
     * @param defValue 默认值
     * @param <T>      类型
     * @return 偏好设置数据
     */
    public static <T> T getValue(String key, T defValue) {
        return Hawk.get(key, defValue);
    }


    /**
     * 根据key删除偏好设置数据
     *
     * @param key key
     */
    public static void remove(String key) {
        if (contains(key)) Hawk.delete(key);
    }

    /**
     * 根据key删除偏好设置数据
     *
     * @param keys 多个key
     */
    public static void remove(String... keys) {
        for (String key : keys)
            Hawk.delete(key);
    }

    /**
     * 清空偏好设置文件
     */
    public static void clear() {
        Hawk.deleteAll();
    }

    /**
     * 根据key比对是否存在key对应的数据
     *
     * @param key key
     * @return 是否存在
     */
    public static boolean contains(String key) {
        return Hawk.contains(key);
    }
}
