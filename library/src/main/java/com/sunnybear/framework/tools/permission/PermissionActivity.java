package com.sunnybear.framework.tools.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.sunnybear.framework.R;

/**
 * 权限获取页面
 * Created by chenkai.gu on 2016/10/20.
 */
public class PermissionActivity extends AppCompatActivity {
    public static final int PERMISSIONS_GRANTED = 0;//权限授权
    public static final int PERMISSIONS_DENIED = 1;//权限拒绝

    private static final int PERMISSION_REQUEST_CODE = 0;//系统权限管理页面的参数
    /*权限参数*/
    private static final String EXTRA_PERMISSIONS = "com.sunnybear.framework.tools.permission.extra_permissions";
    private static final String PACKAGE_URL_SCHEMA = "package:";//方案

    private PermissionsChecker mPermissionsChecker;//权限检测器
    private boolean isRequireCheck;//是否需要系统权限检测,防止和系统提示框重叠

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS))
            throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
        setContentView(R.layout.activity_permission);

        mPermissionsChecker = new PermissionsChecker(this);
        isRequireCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            String[] permissions = getPermissions();
            if (mPermissionsChecker.lacksPermissions(permissions))
                requestPermissions(permissions);//请求权限
            else
                allPermissionsGranted();//全部权限都已获取
        } else {
            isRequireCheck = true;
        }
    }

    /**
     * 启动当前权限页面的公开接口
     *
     * @param activity    activity
     * @param requestCode 请求码
     * @param permissions 权限名列表
     */
    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    /**
     * 返回传递的权限参数
     */
    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    /**
     * 请求权限兼容低版本
     *
     * @param permissions 权限名列表
     */
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    /**
     * 全部权限均已获取
     */
    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        finish();
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            allPermissionsGranted();
        } else {
            isRequireCheck = false;
            showMissingPermissionDialog();
        }
    }

    /**
     * 含有全部的权限
     *
     * @param grantResults 结果
     */
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    /**
     * 显示缺失权限提示
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionActivity.this)
                .setTitle(R.string.help)
                .setMessage(R.string.string_help_text)
                .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(PERMISSIONS_DENIED);
                        finish();
                    }
                })
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSetting();
                    }
                }).setCancelable(false);
        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEMA + getPackageName()));
        startActivity(intent);
    }
}
