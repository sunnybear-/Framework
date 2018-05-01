package com.sunnybear.framework.tools.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * 检查权限的工具类
 * Created by chenkai.gu on 2016/10/20.
 */
public class PermissionsChecker {
    private Context mContext;

    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 判断权限集合
     *
     * @param permissions 权限名列表
     */
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission))
                return true;
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     *
     * @param permission 权限名
     */
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
