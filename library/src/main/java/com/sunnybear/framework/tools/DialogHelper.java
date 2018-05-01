package com.sunnybear.framework.tools;

import android.content.Context;

import com.sunnybear.framework.ui.dialog.SweetAlertDialog;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/22.
 */
public final class DialogHelper {

    /**
     * 消息提示框
     *
     * @param context
     * @param hint
     */
    public static void showHintDialog(Context context, String hint) {
        SweetAlertDialog.getDialog(context, SweetAlertDialog.HINT_TYPE)
                .setTitleText(hint).show();
    }

    /**
     * 警告框
     *
     * @param context
     * @param warn
     * @param warnIcon
     */
    public static void showWarnDialog(Context context, String warn, int warnIcon) {
        SweetAlertDialog.getDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(warn)
                .setCustomImage(warnIcon)
                .showConfirmButton(false).show();
    }

    public static void showErrorDialog(Context context, String error) {
        SweetAlertDialog.getDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(error)
                .showConfirmButton(false).show();
    }

    /**
     * 加载提示框
     *
     * @param context
     * @param loading
     * @param barColor
     * @return
     */
    public static SweetAlertDialog getProgressDialog(Context context, String loading, int barColor) {
        SweetAlertDialog sweetAlertDialog = SweetAlertDialog.getDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(barColor);
        sweetAlertDialog.setTitleText(loading)
                .setDialogClick(false).setCancelable(false);
        return sweetAlertDialog;
    }
}
