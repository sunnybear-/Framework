package com.sunnybear.framework.tools;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.caimuhao.rxpicker.RxPicker;
import com.caimuhao.rxpicker.bean.ImageItem;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;

import java.io.File;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/13.
 */
public final class PictureUtils {

    public final static int REQUEST_CAMERA = 0xFF;

    /**
     * 启动相机
     *
     * @return 照片输出路径
     */
    public static String startCamera(Activity activity, String savePath, String picName, String authority) {
        if (!SDCardUtils.isSDCardEnable()) {
            Toasty.normal(activity, "手机没有储存卡", Toast.LENGTH_LONG).show();
            return null;
        }
        String pic = savePath + picName;
        boolean isCreateSuccess = FileUtils.createMkdirs(savePath);
        if (!isCreateSuccess) {
            Toasty.normal(activity, "照片保存路径创建失败", Toast.LENGTH_LONG).show();
            return null;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(activity, authority, new File(pic)));
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(pic)));
        }
        activity.startActivityForResult(intent, REQUEST_CAMERA);
        return pic;
    }

    /**
     * 图片压缩
     *
     * @param source                 源路径
     * @param outfile                压缩图片输出路径
     * @param fileWithBitmapCallback 压缩回调
     */
    public static void compress(String source, String outfile, FileWithBitmapCallback fileWithBitmapCallback) {
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        options.outfile = outfile;
        Tiny.getInstance().source(source).asFile().withOptions(options)
                .compress(fileWithBitmapCallback);
    }

    /**
     * 启动图库
     *
     * @param activity activity
     * @param isCamera 是否启用拍照
     * @param isSingle 是否单选
     * @param minValue 最小选择张数
     * @param maxValue 最大选择张数
     * @param consumer 选择图片回调
     */
    public static void startGallery(Activity activity, boolean isCamera, boolean isSingle,
                                    int minValue, int maxValue, Consumer<List<ImageItem>> consumer) {
        RxPicker rxPicker = RxPicker.of().single(isSingle).camera(isCamera);
        if (minValue != 0 && maxValue != 0 && minValue < maxValue)
            rxPicker.limit(minValue, maxValue);
        rxPicker.start(activity).subscribe(consumer);
    }

    /**
     * 启动图库
     *
     * @param fragment fragment
     * @param isCamera 是否启用拍照
     * @param isSingle 是否单选
     * @param minValue 最小选择张数
     * @param maxValue 最大选择张数
     * @param consumer 选择图片回调
     */
    public static void startGallery(Fragment fragment, boolean isCamera, boolean isSingle,
                                    int minValue, int maxValue, Consumer<List<ImageItem>> consumer) {
        RxPicker rxPicker = RxPicker.of().single(isSingle).camera(isCamera);
        if (minValue != 0 && maxValue != 0 && minValue < maxValue)
            rxPicker.limit(minValue, maxValue);
        rxPicker.start(fragment).subscribe(consumer);
    }

    /**
     * 启动图库(单选)
     *
     * @param activity activity
     * @param isCamera 是否启用拍照
     * @param consumer 选择图片回调
     */
    public static void startGallery(Activity activity, boolean isCamera, Consumer<List<ImageItem>> consumer) {
        startGallery(activity, isCamera, true, 0, 0, consumer);
    }

    /**
     * 启动图库(单选)
     *
     * @param fragment fragment
     * @param isCamera 是否启用拍照
     * @param consumer 选择图片回调
     */
    public static void startGallery(Fragment fragment, boolean isCamera, Consumer<List<ImageItem>> consumer) {
        startGallery(fragment, isCamera, true, 0, 0, consumer);
    }

    /**
     * 启动图库(多选)
     *
     * @param activity activity
     * @param isCamera 是否启用拍照
     * @param minValue 最小选择张数
     * @param maxValue 最大选择张数
     * @param consumer 选择图片回调
     */
    public static void startGallery(Activity activity, boolean isCamera, int minValue, int maxValue, Consumer<List<ImageItem>> consumer) {
        startGallery(activity, isCamera, false, minValue, maxValue, consumer);
    }

    /**
     * 启动图库(多选)
     *
     * @param fragment fragment
     * @param isCamera 是否启用拍照
     * @param minValue 最小选择张数
     * @param maxValue 最大选择张数
     * @param consumer 选择图片回调
     */
    public static void startGallery(Fragment fragment, boolean isCamera, int minValue, int maxValue, Consumer<List<ImageItem>> consumer) {
        startGallery(fragment, isCamera, false, minValue, maxValue, consumer);
    }
}

