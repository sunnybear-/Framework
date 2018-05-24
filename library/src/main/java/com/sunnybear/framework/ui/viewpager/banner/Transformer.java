package com.sunnybear.framework.ui.viewpager.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.sunnybear.framework.ui.viewpager.banner.transformer.AccordionTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.BackgroundToForegroundTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.CubeInTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.CubeOutTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.DefaultTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.DepthPageTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.FlipHorizontalTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.FlipVerticalTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.ForegroundToBackgroundTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.MeiZuTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.RotateDownTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.RotateUpTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.ScaleInOutTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.StackTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.TabletTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.ZoomInTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.ZoomOutSlideTransformer;
import com.sunnybear.framework.ui.viewpager.banner.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
    public static Class<? extends PageTransformer> MeiZu = MeiZuTransformer.class;
}
