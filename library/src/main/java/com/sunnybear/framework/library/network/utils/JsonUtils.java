package com.sunnybear.framework.library.network.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

/**
 * json工具类
 * Created by guchenkai on 2015/10/27.
 */
public final class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    /**
     * 判断json类型
     *
     * @param json json
     * @return json类型
     */
    public static JsonType getJSONType(String json) {
        if (TextUtils.isEmpty(json))
            return JsonType.JSON_TYPE_ERROR;
        final char[] strChar = json.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];
        switch (firstChar) {
            case '{':
                return JsonType.JSON_TYPE_OBJECT;
            case '[':
                return JsonType.JSON_TYPE_ARRAY;
            default:
                return JsonType.JSON_TYPE_ERROR;
        }
    }

    /**
     * json类型枚举
     */
    public enum JsonType {
        JSON_TYPE_OBJECT,//JSONObject
        JSON_TYPE_ARRAY,//JSONArray
        JSON_TYPE_ERROR//不是JSON格式的字符串
    }

    /**
     * 返回格式化JSON字符串。
     *
     * @param json 未格式化的JSON字符串。
     * @return 格式化的JSON字符串。
     */
    public static String formatJson(String json) {
        StringBuffer result = new StringBuffer();
        int length = json.length();
        int number = 0;
        char key = 0;
        //遍历输入字符串
        for (int i = 0; i < length; i++) {
            //获取当前字符
            key = json.charAt(i);
            //如果当前字符是前方括号,前花括号做如下处理
            if (key == '[' || key == '{') {
                //如果前面还有字符,并且字符为":",打印:换行和缩进字符字符串
                if (i - 1 > 0 && json.charAt(i - 1) == ':')
                    result.append('\n').append(indent(number));
                //打印:当前字符
                result.append(key);
                //前方括号,前花括号,的后面必须换行.打印:换行
                result.append('\n');
                //每出现一次前方括号,前花括号,缩进次数增加一次.打印:新行缩进
                number++;
                result.append(indent(number));
                //进行下一次循环
                continue;
            }
            //如果当前字符是后方括号,后花括号做如下处理
            if (key == ']' || key == '}') {
                //后方括号,后花括号,的前面必须换行.打印:换行
                result.append('\n');
                //每出现一次后方括号,后花括号:缩进次数减少一次.打印:缩进
                number--;
                result.append(indent(number));
                //打印:当前字符
                result.append(key);
                //如果当前字符后面还有字符,并且字符不为",",打印:换行
                if (i + 1 < length && json.charAt(i + 1) != ',')
                    result.append('\n');
                //继续下一次循环
                continue;
            }
            //如果当前字符是逗号.逗号后面换行,并缩进,不改变缩进次数
            if (key == ',') {
                result.append(key).append('\n').append(indent(number));
                continue;
            }
            //打印:当前字符
            result.append(key);
        }
        return result.toString();
    }

    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     *
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private static String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append("   ");
        }
        return result.toString();
    }

    /**
     * 解析json
     *
     * @param json
     * @param mClass
     * @param <T>
     * @return
     */
    public static <T> T parseJson(String json, Class<?> mClass) {
        T result = null;
        JsonUtils.JsonType type = JsonUtils.getJSONType(json);
        switch (type) {
            case JSON_TYPE_OBJECT://对象类型的json
                result = (T) JSON.parseObject(json, mClass);
                break;
            case JSON_TYPE_ARRAY://数组类型的json
                result = (T) JSON.parseArray(json, mClass);
                break;
            case JSON_TYPE_ERROR://不是json
                throw new IllegalArgumentException("返回数据不是json类型,请尝试使用StringCallback接收");
        }
        return result;
    }
}