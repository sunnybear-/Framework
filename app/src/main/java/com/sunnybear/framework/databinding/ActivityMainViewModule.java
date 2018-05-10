package com.sunnybear.framework.databinding;

import android.arch.lifecycle.LifecycleOwner;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.task.XExecutor;
import com.sunnybear.framework.R;
import com.sunnybear.framework.library.base.BaseViewModule;
import com.sunnybear.framework.module.MainActivity;
import com.sunnybear.framework.tools.AppUtils;
import com.sunnybear.framework.tools.SDCardUtils;
import com.sunnybear.framework.tools.Toasty;
import com.sunnybear.framework.ui.dialog.LoadingDialog;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.upgrade.UpgradeListener;

import java.io.File;

/**
 * <p>
 * Created by chenkai.gu on 2018/4/29.
 */
public class ActivityMainViewModule extends BaseViewModule<MainActivity, ActivityMainBinding>
        implements View.OnClickListener, XExecutor.OnAllTaskEndListener {

    private LoadingDialog mLoadingDialog;

    private OkDownload mDownload;

    public ActivityMainViewModule(MainActivity mainActivity, ActivityMainBinding viewDataBinding) {
        super(mainActivity, viewDataBinding);
    }

    @Override
    public void init() {
        mViewDataBinding.setVm(this);
        //加载框
        mLoadingDialog = LoadingDialog.getBuilder(mContext)
                .setMessage("下载中...")
                .setCancelable(false)
                .setCancelOutside(false).create();

        mDownload = OkDownload.getInstance();
        mDownload.setFolder(SDCardUtils.getSDCardPath() + "/download/");
        mDownload.addOnAllTaskEndListener(this);

        Beta.upgradeListener = new UpgradeListener() {
            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                if (strategy != null) {
                    String apkUrl = strategy.apkUrl;
                    OkDownload.request("check_apk", OkGo.<File>get(apkUrl))
                            .fileName("framewor.apk")
                            .save()
                            .register(new DownloadListener("check_apk") {
                                @Override
                                public void onStart(Progress progress) {
                                    mLoadingDialog.show();
                                }

                                @Override
                                public void onProgress(Progress progress) {

                                }

                                @Override
                                public void onError(Progress progress) {
                                    mLoadingDialog.dismiss();
                                }

                                @Override
                                public void onFinish(File file, Progress progress) {
                                    mLoadingDialog.dismiss();
                                    AppUtils.installApk(mContext, file);
                                }

                                @Override
                                public void onRemove(Progress progress) {

                                }
                            }).start();
                } else {
                    Toasty.normal(mContext, "没有更新", Toast.LENGTH_LONG).show();
                }
            }
        };
        Beta.autoCheckUpgrade = false;
        Bugly.init(mContext, "9d28f50b19", true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map:
//                StartHelper.with(mContext).startActivity(MapActivity.class);
//                StartHelper.with(mContext).startActivity(UrlReceiveActivity.class,
//                        "arouter://sunnybear/framework/map" +
//                                "?name=alex&age=18&boy=true&high=180&obj=%7b%22name%22%3a%22jack%22%2c%22id%22%3a666%7d");
                ARouter.getInstance()
                        .build(Uri.parse("arouter://sunnybear/framework/map" +
                                "?name=alex&age=18&boy=true&high=180&obj=%7b%22name%22%3a%22jack%22%2c%22id%22%3a666%7d"))
                        .navigation();
                break;
            case R.id.btn_check_version:
                Beta.checkUpgrade(true, true);
                break;
        }
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        super.onDestroy(owner);
        mDownload.removeOnAllTaskEndListener(this);
    }

    @Override
    public void onAllTaskEnd() {

    }
}
