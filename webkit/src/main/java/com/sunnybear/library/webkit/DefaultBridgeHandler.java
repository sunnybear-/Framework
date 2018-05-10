package com.sunnybear.library.webkit;

import android.util.Log;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;

/**
 * 默认的BridgeHandler
 * Created by chenkai.gu on 2018/5/10.
 */
public class DefaultBridgeHandler implements BridgeHandler {

    private static final String TAG = "DefaultBridgeHandler";

    @Override
    public void handler(String data, CallBackFunction function) {
        Log.d(TAG, "Handler接受数据:" + data);
        if (function != null)
            function.onCallBack("DefaultHandler response data");
    }
}
