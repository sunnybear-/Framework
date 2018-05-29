package com.sunnybear.framework.library.router;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sunnybear.framework.tools.log.Logger;

/**
 * 网页路由跳转策略
 * Created by chenkai.gu on 2018/5/10.
 */
public class RouterTransferStrategy extends AppCompatActivity {

    private static final String TAG = "ARouter";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().build(getIntent().getData())
                .navigation(this, new NavCallback() {
                    /**
                     * 跳转完成
                     * @param postcard
                     */
                    @Override
                    public void onArrival(Postcard postcard) {
                        Logger.i(TAG, "跳转完成");
                        finish();
                    }

                    /**
                     * 找不到路由目标
                     * @param postcard
                     */
                    @Override
                    public void onLost(Postcard postcard) {
                        Logger.e(TAG, "没有找到路由目标");
                    }

                    /**
                     * 找到路由目标
                     * @param postcard
                     */
                    @Override
                    public void onFound(Postcard postcard) {

                    }

                    /**
                     * 被拦截器拦截
                     * @param postcard
                     */
                    @Override
                    public void onInterrupt(Postcard postcard) {

                    }
                });
    }
}
