package com.sunnybear.framework.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sunnybear.framework.tools.permission.PermissionActivity;
import com.sunnybear.framework.tools.permission.PermissionsChecker;

/**
 * 基础欢迎页
 * Created by chenkai.gu on 2018/5/14.
 */
public abstract class BaseWelcomeActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 0xFF;

    /*权限检查器*/
    private PermissionsChecker mPermissionsChecker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPermissionsChecker = new PermissionsChecker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionsChecker.lacksPermissions(getPermissions())) {
            PermissionActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, getPermissions());
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionActivity.PERMISSIONS_DENIED)
            finish();
    }

    /**
     * 设置布局id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 设置申请权限列表
     *
     * @return
     */
    public abstract String[]  getPermissions();
}
