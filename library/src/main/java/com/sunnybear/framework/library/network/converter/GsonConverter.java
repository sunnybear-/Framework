package com.sunnybear.framework.library.network.converter;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.utils.OkLogger;
import com.sunnybear.framework.library.network.utils.JsonUtils;
import com.sunnybear.framework.tools.log.Logger;

import java.io.Serializable;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * <p>
 * Created by chenkai.gu on 2018/2/9.
 */
public class GsonConverter<T> implements Converter<T> {
    private Gson mGson;
    private Class<? extends Serializable> mClass;
    private Type mType;

    public GsonConverter(Class<? extends Serializable> aClass) {
        mClass = aClass;
        mGson = new Gson();
    }

    public void setType(Type type) {
        mType = type;
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
                result = (T) mGson.fromJson(json, mClass);
                break;
            case JSON_TYPE_ARRAY://数组类型的json
                printJson(json);
                result = mGson.fromJson(json, $Gson$Types.canonicalize(mType));
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
