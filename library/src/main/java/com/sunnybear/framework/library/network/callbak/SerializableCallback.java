package com.sunnybear.framework.library.network.callbak;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.model.Response;
import com.sunnybear.framework.library.network.converter.FastJsonConverter;
import com.sunnybear.framework.library.network.converter.GsonConverter;
import com.sunnybear.framework.tools.ReflectUtils;
import com.sunnybear.framework.tools.log.Logger;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 返回Serializable类型的数据
 * Created by chenkai.gu on 2018/1/13.
 */
public abstract class SerializableCallback<T> extends AbsCallback<T> {
    private Converter<T> mConverter;

    public SerializableCallback() {
        this(false);
    }

    public SerializableCallback(boolean isGson) {
        if (isGson) {
            mConverter = new GsonConverter<>(ReflectUtils.getGenericClass(this.getClass()));
            ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
            ((GsonConverter) mConverter).setType(parameterizedType.getActualTypeArguments()[0]);
        } else {
            mConverter = new FastJsonConverter<>(ReflectUtils.getGenericClass(this.getClass()));
        }
    }

    public SerializableCallback(Class<? extends Serializable> mClass) {
        mConverter = new FastJsonConverter<>(mClass);
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        T result = mConverter.convertResponse(response);
        response.close();
        return result;
    }

    @Override
    public final void onSuccess(Response<T> response) {
        onSuccess(response.body());
    }

    @Override
    public void onCacheSuccess(Response<T> response) {
        onCacheSuccess(response.body());
    }

    public abstract void onSuccess(T t);

    public void onCacheSuccess(T t) {

    }

    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        Throwable throwable = response.getException();
        if (throwable instanceof SocketTimeoutException || throwable instanceof ConnectException) {
            Logger.e(throwable.getMessage());
            return;
        }
    }
}
