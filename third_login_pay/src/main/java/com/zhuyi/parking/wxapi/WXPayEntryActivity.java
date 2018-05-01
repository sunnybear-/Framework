package com.zhuyi.parking.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sunnybear.framework.library.eventbus.EventBusHelper;
import com.sunnybear.framework.library.eventbus.EventBusMessage;
import com.sunnybear.framework.tools.log.Logger;
import com.sunnybear.library.third.Config;
import com.sunnybear.library.third.wechat.WeChatApi;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeChatApi.registerWXAPI(this, Config.APP_WECHAT_ID);
        WeChatApi.getWXApi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WeChatApi.getWXApi().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Logger.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == BaseResp.ErrCode.ERR_OK)
                EventBusHelper.post(EventBusMessage.assembleMessage(Config.KEY_WECHAT_PAY_SUCCESS, resp));
            else
                EventBusHelper.post(EventBusMessage.assembleMessage(Config.KEY_WECHAT_PAY_FAIL, resp));
            finish();
        }
    }
}