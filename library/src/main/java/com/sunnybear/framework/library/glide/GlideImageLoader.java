package com.sunnybear.framework.library.glide;

import android.widget.ImageView;

import com.caimuhao.rxpicker.utils.RxPickerImageLoader;

/**
 * 图库加载器
 * Created by chenkai.gu on 2018/3/13.
 */
public class GlideImageLoader implements RxPickerImageLoader {

    @Override
    public void display(ImageView imageView, String path, int width, int height) {
        ImageLoader.with(imageView.getContext())
                .load(path).centerCrop()
                .override(320, 320)
                .into(imageView);
    }
}
