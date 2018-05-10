package com.sunnybear.library.webkit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/7.
 */
@Route(path = Constant.ROUTER_WEB, group = Constant.GROUP)
public class WebKitFragment extends Fragment {

    protected Context mContext;
    private View mFragmentView;

    protected BridgeWebView mBridgeWebView;
    private ProgressBar mProgressBar;

    private DefaultBridgeHandler mBridgeHandler;
    private BridgeWebViewClient mBridgeWebViewClient;

    public BridgeWebView getBridgeWebView() {
        return mBridgeWebView;
    }

    public void setBridgeHandler(DefaultBridgeHandler bridgeHandler) {
        mBridgeHandler = bridgeHandler;
    }

    public void setBridgeWebViewClient(BridgeWebViewClient bridgeWebViewClient) {
        mBridgeWebViewClient = bridgeWebViewClient;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_web, container, false);
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null)
            parent.removeView(mFragmentView);
        mBridgeWebView = mFragmentView.findViewById(R.id.web_page);
        mProgressBar = mFragmentView.findViewById(R.id.progress_web);
        return mFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mBridgeWebView != null) {
            mBridgeWebView.setDefaultHandler(mBridgeHandler);
            mBridgeWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (mProgressBar != null) {
                        mProgressBar.setProgress(newProgress);
                        if (newProgress == 100)
                            mProgressBar.setVisibility(View.GONE);
                    }
                }
            });
            mBridgeWebView.setWebViewClient(mBridgeWebViewClient);
        }
    }

    /**
     * 加载网页
     *
     * @param url
     */
    public void load(String url) {
        if (mBridgeWebView != null)
            mBridgeWebView.loadUrl(url);
    }

    /**
     * 网页是否可以返回
     *
     * @return
     */
    public boolean canGoBack() {
        return mBridgeWebView.canGoBack();
    }

    /**
     * 返回上一页
     */
    public void goBack() {
        if (canGoBack())
            mBridgeWebView.goBack();
    }
}
