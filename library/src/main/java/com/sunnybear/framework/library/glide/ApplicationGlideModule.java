package com.sunnybear.framework.library.glide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.sunnybear.framework.library.base.BaseApplication;
import com.sunnybear.framework.tools.SDCardUtils;

import java.io.InputStream;

/**
 * Glide全局配置
 * Created by chenkai.gu on 2018/3/5.
 */
@GlideModule(glideName = "ImageLoader")
public final class ApplicationGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .setBitmapPoolScreens(3)
                .build();
        /*内存缓存*/
//        int memoryCacheSizeBytes = 1024 * 1024 * 20;//内存缓存为20MB
//        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        /*Bitmap池*/
//        int bitmapPoolSizeBytes = 1024 * 1024 * 30;
//        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));
        builder.setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()));
        /*磁盘缓存*/
        int diskCacheSizeBytes = 1024 * 1024 * 1024;//1GB的磁盘缓存
        builder.setDiskCache(new DiskLruCacheFactory(getStorageDirectory(context), diskCacheSizeBytes));
        /*默认请求选项*/
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888).disallowHardwareConfig());
        /*未捕获异常策略*/
        /*日志级别*/
        builder.setLogLevel(Log.DEBUG);
    }

    /**
     * 获取磁盘储存路径
     *
     * @return
     */
    private String getStorageDirectory(Context context) {
        return SDCardUtils.isSDCardEnable() ? SDCardUtils.getSDCardPath() + "/" + BaseApplication.getPkgName() + "/image" :
                context.getCacheDir().getAbsolutePath() + "/" + BaseApplication.getPkgName() + "/image";
    }
}
