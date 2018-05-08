package com.sunnybear.library.webkit;

import android.content.Context;

import com.github.lzyzsd.jsbridge.BridgeWebView;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/8.
 */
public interface Handler {

    /**
     * JavaScript调用Android Native
     *
     * @param context
     * @param bridgeWebView
     */
    void registerHandler(Context context, BridgeWebView bridgeWebView);

    /**
     * Android Native调用JavaScript
     *
     * @param context
     * @param bridgeWebView
     */
    void callHandler(Context context, BridgeWebView bridgeWebView);
}
