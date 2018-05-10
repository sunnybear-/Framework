package com.sunnybear.library.webkit;

import android.content.Context;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 默认的WebChromeClient
 * Created by chenkai.gu on 2018/5/10.
 */
public class DefaultWebChromeClient extends WebChromeClient {

    protected Context mContext;

    private ProgressBar mProgressBar;

    public DefaultWebChromeClient(Context context, View rootView) {
        mContext = context;
        mProgressBar = rootView.findViewById(R.id.progress_web);
    }

    public DefaultWebChromeClient(Context context) {
        mContext = context;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (mProgressBar != null) {
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100)
                mProgressBar.setVisibility(View.GONE);
        }
    }
}
