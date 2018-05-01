package com.sunnybear.framework.tools;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/9.
 */
public final class PermissionsHelper {

    private RxPermissions mRxPermissions;
    private Observable<Permission> mPermissionObservable;

    private PermissionsHelper(Activity activity) {
        mRxPermissions = new RxPermissions(activity);
    }

    public static PermissionsHelper with(Activity activity) {
        return new PermissionsHelper(activity);
    }

    /**
     * 添加权限
     *
     * @param permissions 权限集
     */
    public PermissionsHelper permissions(String... permissions) {
        mPermissionObservable = mRxPermissions.requestEach(permissions);
        return this;
    }

    /**
     * 申请权限
     *
     * @param lifecycleTransformer      生命周期
     * @param onCheckPermissionCallback 检查权限回调监听
     */
    public void apply(LifecycleTransformer<Permission> lifecycleTransformer, @Nullable final onCheckPermissionCallback onCheckPermissionCallback) {
        mPermissionObservable
                .compose(lifecycleTransformer)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (onCheckPermissionCallback != null)
                            onCheckPermissionCallback.onCheckPermission(permission);
                    }
                });
    }

    /**
     * 申请权限
     *
     * @param onCheckPermissionCallback 检查权限回调监听
     */
    public void apply(@Nullable final onCheckPermissionCallback onCheckPermissionCallback) {
        mPermissionObservable
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (onCheckPermissionCallback != null)
                            onCheckPermissionCallback.onCheckPermission(permission);
                    }
                });
    }

    /**
     * 检查权限回调
     */
    public interface onCheckPermissionCallback {

        void onCheckPermission(Permission permission);
    }
}
