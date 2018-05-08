package com.sunnybear.framework.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.sunnybear.framework.tools.Toasty;
import com.sunnybear.library.webkit.Handler;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/8.
 */
public class WebHandler implements Handler {
    @Override
    public void registerHandler(final Context context, BridgeWebView bridgeWebView) {
        bridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String str = "这是html返回给java的数据:" + data;
                Toasty.normal(context, str).show();
                //回调返回给Js
                function.onCallBack("这是Native返回的数据:" + data);
            }
        });
        bridgeWebView.registerHandler("functionOpen", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Toasty.normal(context, "网页在打开你的下载文件预览").show();
                Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
                chooserIntent.setType("image/*");
                ((Activity) context).startActivityForResult(chooserIntent, 1);
            }
        });
    }

    @Override
    public void callHandler(final Context context, BridgeWebView bridgeWebView) {
        bridgeWebView.callHandler("functionInJs", "Android Native传递的消息",
                new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Toasty.normal(context, "网页在获取你的位置，" + data).show();
                    }
                });
        bridgeWebView.send("Hello",
                new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Toasty.normal(context, data).show();
                    }
                });
    }
}
