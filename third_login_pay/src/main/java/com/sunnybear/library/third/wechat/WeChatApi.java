package com.sunnybear.library.third.wechat;

import android.content.Context;
import android.widget.Toast;

import com.sunnybear.framework.tools.DialogHelper;
import com.sunnybear.framework.tools.Toasty;
import com.sunnybear.library.third.Config;
import com.sunnybear.library.third.wechat.entity.WXPayInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信API
 * Created by chenkai.gu on 2018/3/20.
 */
public final class WeChatApi {

    private static IWXAPI mWXApi;

    public static IWXAPI getWXApi() {
        return mWXApi;
    }

    /**
     * 注册微信api
     *
     * @param context
     */
    public static void registerWXAPI(Context context, String appId) {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWXApi = WXAPIFactory.createWXAPI(context, appId, false);
        //将该app注册到微信,参数是指你应用在微信开放平台上的AppID
        mWXApi.registerApp(appId);
    }

    /**
     * 微信登录
     *
     * @param context
     */
    public static void login(Context context) {
        if (mWXApi != null) {
            if (!mWXApi.isWXAppInstalled()) {
                DialogHelper.showHintDialog(context, "您还未安装微信客户端");
                return;
            }
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "diandi_wx_login";
            mWXApi.sendReq(req);
        } else {
            Toasty.normal(context, "WXAPI未注册", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 微信支付
     *
     * @param context
     */
    public static void pay(Context context, WXPayInfo info) {
        if (mWXApi != null) {
            if (!mWXApi.isWXAppInstalled()) {
                DialogHelper.showHintDialog(context, "您还未安装微信客户端");
                return;
            }
            PayReq req = new PayReq();
            req.appId = Config.APP_WECHAT_ID;
            req.partnerId = info.getPartnerId();
            req.prepayId = info.getPrepayId();
            req.packageValue = info.getPackageValue();
            req.nonceStr = info.getNonceStr();
            req.timeStamp = info.getTimeStamp();
            req.sign = info.getSign();
            req.extData = "add data";
            mWXApi.sendReq(req);
        }
    }
}