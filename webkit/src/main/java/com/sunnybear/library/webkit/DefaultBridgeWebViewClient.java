package com.sunnybear.library.webkit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

/**
 * 默认的BridgeWebViewClient
 * Created by chenkai.gu on 2018/5/10.
 */
public class DefaultBridgeWebViewClient extends BridgeWebViewClient {

    protected Context mContext;

    public DefaultBridgeWebViewClient(Context context, BridgeWebView webView) {
        super(webView);
        mContext = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(Constant.ROUTER_SCHEME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(intent);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}
