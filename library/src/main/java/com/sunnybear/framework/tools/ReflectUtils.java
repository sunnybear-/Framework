package com.sunnybear.framework.tools;

import android.support.annotation.Nullable;

import com.lzy.okgo.utils.OkLogger;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 反射工具
 * Created by chenkai.gu on 2018/2/1.
 */
public final class ReflectUtils {

    /**
     * 获取私有属性字段
     *
     * @param target    反射目标
     * @param fieldName 字段名
     */
    public static Object getPrivateField(Object target, String fieldName) {
        Object result = null;
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            result = field.get(target);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 修改私有属性值
     *
     * @param target    反射目标
     * @param fieldName 字段名
     * @param modify    修改属性对象
     */
    public static void modifyPrivateField(Object target, String fieldName, Object modify) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, modify);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用私有方法
     *
     * @param target         反射目标
     * @param methodName     方法名
     * @param parameterTypes 方法参数类型
     * @param args           方法参数值
     */
    public static Object getPrivateMethod(Object target, String methodName,
                                          @Nullable Class<?>[] parameterTypes, @Nullable Object[] args) {
        Object result = null;
        try {
            Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            result = method.invoke(target, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取父类的泛型类型
     *
     * @return
     */
    public static Class<? extends Serializable> getGenericClass(Class<?> target) {
        Type genType = target.getGenericSuperclass();
        Type generic = ((ParameterizedType) genType).getActualTypeArguments()[0];
        if (!(generic instanceof Class))//泛型为array类型
            try {
                Field mArgs = generic.getClass().getDeclaredField("args");
                mArgs.setAccessible(true);
                Object o = mArgs.get(generic);

                Field mTypes = o.getClass().getDeclaredField("types");
                mTypes.setAccessible(true);
                ArrayList list = (ArrayList) mTypes.get(o);

                Object o1 = list.get(0);
                Field mRawType = o1.getClass().getDeclaredField("rawType");
                mRawType.setAccessible(true);
                return (Class<? extends Serializable>) mRawType.get(o1);
            } catch (Exception e) {
                OkLogger.e(e.getMessage());
                return null;
            }
        else//泛型为object类型
            return (Class<? extends Serializable>) generic;
    }
}
