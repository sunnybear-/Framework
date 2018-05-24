package com.sunnybear.framework.ui.viewpager.banner.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/24.
 */
public class MeiZuTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9F;

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1)
            page.setScaleY(MIN_SCALE);
        else if (position <= 1)
            page.setScaleY(Math.max(MIN_SCALE, 1 - Math.abs(position)));
        else
            page.setScaleY(MIN_SCALE);
    }
}
