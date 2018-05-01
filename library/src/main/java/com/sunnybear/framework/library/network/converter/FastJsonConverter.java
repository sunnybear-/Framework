package com.sunnybear.framework.library.network.converter;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.utils.OkLogger;
import com.sunnybear.framework.library.network.utils.JsonUtils;
import com.sunnybear.framework.tools.log.Logger;

import java.io.Serializable;

import okhttp3.Response;

/**
 * Json类型转换器
 * Created by chenkai.gu on 2018/1/13.
 */
public class FastJsonConverter<T> implements Converter<T> {
    private Class<? extends Serializable> mClass;

    public FastJsonConverter(Class<? extends Serializable> aClass) {
        mClass = aClass;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        if (mClass == null) throw new IllegalStateException("获取解析器泛型类型发生错误");
        T result = null;
        String json = response.body().string();
        JsonUtils.JsonType type = JsonUtils.getJSONType(json);
        switch (type) {
            case JSON_TYPE_OBJECT://对象类型的json
                printJson(json);
                result = (T) JSON.parseObject(json, mClass);
                break;
            case JSON_TYPE_ARRAY://数组类型的json
                printJson(json);
                result = (T) JSON.parseArray(json, mClass);
                break;
            case JSON_TYPE_ERROR://不是json
                throw new IllegalArgumentException("返回数据不是json类型,请尝试使用StringCallback接收");
        }
        return result;
    }

    /**
     * 打印json
     *
     * @param json json
     */
    private void printJson(String json) {
        OkLogger.i(JsonUtils.formatJson(json));
        Logger.json("OkGo", json);
    }
}
