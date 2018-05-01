package com.zhuyi.parking.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.sunnybear.framework.library.eventbus.EventBusHelper;
import com.sunnybear.framework.library.eventbus.EventBusMessage;
import com.sunnybear.framework.library.network.callbak.SerializableCallback;
import com.sunnybear.framework.tools.log.Logger;
import com.sunnybear.framework.ui.dialog.LoadingDialog;
import com.sunnybear.library.third.Config;
import com.sunnybear.library.third.wechat.WeChatApi;
import com.sunnybear.library.third.wechat.entity.WXAccessToken;
import com.sunnybear.library.third.wechat.entity.WXBaseResp;
import com.sunnybear.library.third.wechat.entity.WXUserInfo;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * 用来接收微信的响应信息
 * Created by chenkai.gu on 2018/3/20.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXAPI";

    //    private SweetAlertDialog mProgressDialog;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeChatApi.registerWXAPI(this, Config.APP_WECHAT_ID);

        boolean result = WeChatApi.getWXApi().handleIntent(getIntent(), this);
        if (!result) {
            Logger.e("参数不合法,未被SDK处理,退出");
            finish();
        }

        //加载提示框框
        mLoadingDialog = LoadingDialog.getBuilder(this)
                .setMessage("微信登录中...")
                .setCancelable(false)
                .setCancelOutside(false).create();
    }

    @Override
    protected void onStop() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WeChatApi.getWXApi().handleIntent(data, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WeChatApi.getWXApi().handleIntent(intent, this);
    }

    /**
     * 微信发送请求到第三方应用时，会回调到该方法
     *
     * @param baseReq
     */
    @Override
    public void onReq(BaseReq baseReq) {
        Logger.d(TAG, "baseReq:" + JSON.toJSONString(baseReq));
    }

    /**
     * 第三方应用发送到微信的请求处理后的响应结果,会回调到该方法
     * app发送消息给微信,处理返回消息的回调
     *
     * @param baseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {
        Logger.d(TAG, "baseResp:" + JSON.toJSONString(baseResp));
        Logger.d(TAG, "baseResp:" + baseResp.errStr + "," + baseResp.openId + "," + baseResp.transaction + "," + baseResp.errCode);
        WXBaseResp entity = JSON.parseObject(JSON.toJSONString(baseResp), WXBaseResp.class);
        switch (baseResp.getType()) {
            case ConstantsAPI.COMMAND_SENDAUTH://微信登录
                weChatLogin(baseResp, entity);
                break;
        }
    }

    /**
     * 微信登录
     *
     * @param baseResp
     * @param entity
     */
    private void weChatLogin(BaseResp baseResp, WXBaseResp entity) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                OkGo.<WXAccessToken>get("https://api.weixin.qq.com/sns/oauth2/access_token")
                        .params("appid", Config.APP_WECHAT_ID)
                        .params("secret", Config.APP_WECHAT_SECRET)
                        .params("code", entity.getCode())
                        .params("grant_type", "authorization_code")
                        .execute(new SerializableCallback<WXAccessToken>() {

                            @Override
                            public void onStart(Request<WXAccessToken, ? extends Request> request) {
                                mLoadingDialog.show();
                            }

                            @Override
                            public void onSuccess(WXAccessToken wxAccessToken) {
                                if (wxAccessToken != null)
                                    getUserInfo(wxAccessToken);
                                else
                                    Logger.e(TAG, "获取失败..");
                            }

                            @Override
                            public void onError(Response<WXAccessToken> response) {
                                super.onError(response);
                                Logger.e(TAG, "请求错误..");
                            }
                        });
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                finish();
                break;
            case BaseResp.ErrCode.ERR_BAN:
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    /**
     * 获取微信用户信息
     *
     * @param wxAccessToken
     */
    private void getUserInfo(WXAccessToken wxAccessToken) {
        OkGo.<WXUserInfo>get("https://api.weixin.qq.com/sns/userinfo")
                .params("access_token", wxAccessToken.getAccess_token())
                .params("openid", wxAccessToken.getOpenid())
                .execute(new SerializableCallback<WXUserInfo>() {
                    @Override
                    public void onSuccess(WXUserInfo wxUserInfo) {
                        Logger.d(TAG, "微信登录资料已获取，后续未完成");
                        EventBusHelper.post(EventBusMessage.assembleMessage(Config.KEY_WECHAT_USER_INFO, wxUserInfo));
                        finish();
                    }

                    @Override
                    public void onError(Response<WXUserInfo> response) {
                        super.onError(response);
                        Logger.e(TAG, "获取错误..");
                    }

                    @Override
                    public void onFinish() {
                        if (mLoadingDialog != null && mLoadingDialog.isShowing())
                            mLoadingDialog.dismiss();
                    }
                });
    }
}
