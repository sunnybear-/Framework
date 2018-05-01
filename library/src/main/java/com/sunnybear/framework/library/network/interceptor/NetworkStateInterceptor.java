package com.sunnybear.framework.library.network.interceptor;

import com.sunnybear.framework.library.base.BaseApplication;
import com.sunnybear.framework.tools.PhoneUtil;
import com.sunnybear.framework.tools.Toasty;
import com.sunnybear.framework.tools.log.Logger;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 网络状态拦截器
 * Created by chenkai.gu on 2018/3/28.
 */
public class NetworkStateInterceptor implements Interceptor {

    private String TAG = "OkGo";

    public NetworkStateInterceptor() {
    }

    public NetworkStateInterceptor(String TAG) {
        this.TAG = TAG;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        boolean isNetworkConnection = PhoneUtil.isNetworkConnected(BaseApplication.getINSTANCE());
        if (!isNetworkConnection) {
            Logger.e(TAG, "没有网络连接");
            Flowable.just("123")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Toasty.error(BaseApplication.getINSTANCE(), "没有网络连接").show();
                        }
                    });
            return null;
        }
        Response response = chain.proceed(chain.request());
        return response;
    }
}
