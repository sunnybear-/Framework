package com.sunnybear.library.third.alipay;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.sunnybear.framework.tools.Toasty;
import com.sunnybear.framework.tools.log.Logger;
import com.sunnybear.library.third.alipay.entity.AuthResult;
import com.sunnybear.library.third.alipay.entity.PayResult;

import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付宝API
 * Created by chenkai.gu on 2018/3/20.
 */
public final class AliPayApi {
    private static final String TAG = "Alipay";

    /**
     * 支付宝登录调用方法
     *
     * @param context  activity
     * @param userInfo 用户信息
     */
    public static void login(final Activity context, final String userInfo, final onAliPayAuthCallback onAliPayAuthCallback) {
        Flowable.create(new FlowableOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(FlowableEmitter<AuthResult> emitter) throws Exception {
                //构造AuthTask对象
                AuthTask alipay = new AuthTask(context);
                //调用授权接口,获得授权结果
                Map<String, String> result = alipay.authV2(userInfo, true);
                AuthResult authResult = new AuthResult(result, true);
                emitter.onNext(authResult);

            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthResult>() {
                    @Override
                    public void accept(AuthResult authResult) throws Exception {
                        String result = authResult.getResultStatus();
                        if (onAliPayAuthCallback != null) {
                            // 判断resultStatus 为“9000”且result_code
                            // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                            if (TextUtils.equals(result, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                                // 获取alipay_open_id,调支付时作为参数extern_token的value传入,则支付账户为该授权账户
                                Logger.i("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                                onAliPayAuthCallback.onSuccess(authResult);
                            } else {
                                // 其他状态值则为授权失败
                                Logger.e("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                                onAliPayAuthCallback.onFail();
                            }
                        }
                    }
                });
    }

    /**
     * 支付宝授权登录回调
     */
    public interface onAliPayAuthCallback {

        void onSuccess(AuthResult authResult);

        void onFail();
    }

    /**
     * 支付宝支付调用方法
     *
     * @param context   activity
     * @param orderInfo 订单信息
     */
    public static void pay(final Activity context, final String orderInfo, final OnAliPayCallback callback) {
        Flowable.create(new FlowableOnSubscribe<PayResult>() {
            @Override
            public void subscribe(FlowableEmitter<PayResult> emitter) throws Exception {
                //构造PayTask对象
                PayTask alipay = new PayTask(context);
                //调用支付接口,获取支付结果
                String result = alipay.pay(orderInfo, true);
                PayResult payResult = new PayResult(result);
                emitter.onNext(payResult);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PayResult>() {
                    @Override
                    public void accept(PayResult payResult) throws Exception {
                        /**
                         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                         * docType=1) 建议商户依赖异步通知
                         */
                        String resultInfo = payResult.getResult();
                        Logger.d(TAG, resultInfo);
                        String resultStatus = payResult.getResultStatus();
                        if (callback != null) {
                            switch (resultStatus) {
                                //判断resultStatus为"9000"则代表支付成功,具体状态码代表含义可参考接口文档
                                case "9000":
//                                    Toasty.normal(context, "支付宝支付成功", Toast.LENGTH_LONG).show();
                                    callback.onSuccess(payResult);
                                    break;
                                //"8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认,最终交易是否成功以服务端异步通知为准(小概率状态)
                                case "8000":
//                                    Toasty.normal(context, "支付结果确认中", Toast.LENGTH_LONG).show();
                                    break;
                                //其他值就可以判断为支付失败,包括用户主动取消支付,或者系统返回的错误
                                default:
                                    Toasty.normal(context, "支付宝支付失败", Toast.LENGTH_LONG).show();
                                    callback.onFail();
                                    break;
                            }
                        }
                    }
                });
    }

    /**
     * 支付宝支付回调
     */
    public interface OnAliPayCallback {

        void onSuccess(PayResult payResult);

        void onFail();
    }
}
