package com.sunnybear.framework.provider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sunnybear.framework.library.router.RouterServiceProvider;
import com.sunnybear.framework.tools.Toasty;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/17.
 */
@Route(path = "/service/test")
public class ARouterTestService extends RouterServiceProvider {

    public void printToast(String message) {
        Toasty.normal(mApplicationContext, message).show();
    }
}
