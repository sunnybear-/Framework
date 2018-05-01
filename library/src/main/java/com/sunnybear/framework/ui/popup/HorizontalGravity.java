package com.sunnybear.framework.ui.popup;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/19.
 */
@IntDef({
        HorizontalGravity.CENTER,
        HorizontalGravity.LEFT,
        HorizontalGravity.RIGHT,
        HorizontalGravity.ALIGN_LEFT,
        HorizontalGravity.ALIGN_RIGHT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface HorizontalGravity {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;
}
