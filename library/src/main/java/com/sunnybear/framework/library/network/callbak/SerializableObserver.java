package com.sunnybear.framework.library.network.callbak;

import com.sunnybear.framework.tools.log.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/26.
 */
public abstract class SerializableObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException || throwable instanceof ConnectException) {
            Logger.e(throwable.getMessage());
            return;
        }
    }

    @Override
    public void onComplete() {

    }
}
