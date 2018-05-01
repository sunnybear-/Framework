package com.sunnybear.framework.library.glide;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/11.
 */
@GlideExtension
public final class LibraryGlideExtension {
    //缩略图的最小尺寸，单位：px
    private static final int MINI_THUMB_SIZE = 100;

    private LibraryGlideExtension() {
    }

    /**
     * 1.自己新增的方法的第一个参数必须是RequestOptions options
     * 2.方法必须是静态的
     *
     * @param options
     */
    @GlideOption
    public static void miniThumb(RequestOptions options) {
        options.fitCenter().override(MINI_THUMB_SIZE);
    }
}
