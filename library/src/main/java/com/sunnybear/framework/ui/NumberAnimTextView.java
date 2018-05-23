package com.sunnybear.framework.ui;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * 数字增加动画的　TextView
 *
 * @author sunnybear
 * @date 16-11-26
 */
public class NumberAnimTextView extends android.support.v7.widget.AppCompatTextView {

    /**
     * 起始值 默认 0
     */
    private static String mNumStart = "0";
    /**
     * 结束值
     */
    private String mNumEnd;
    /**
     * 动画总时间 默认 2000 毫秒
     */
    private long mDuration = 2000;
    /**
     * 前缀
     */
    private String mPrefix = "";
    /**
     * 后缀
     */
    private String mPostfix = "";
    /**
     * 是否开启动画
     */
    private boolean isEnableAnim = true;
    /**
     * 是否是整数
     */
    private boolean isInt;
    private ValueAnimator animator;

    public NumberAnimTextView(Context context) {
        this(context, null, -1);
    }

    public NumberAnimTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NumberAnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNumber(String number) {
        setNumber("0", number);
    }

    public void setNumber(String numberStart, String numberEnd) {
        mNumStart = numberStart;
        mNumEnd = numberEnd;
        if (checkNumString(numberStart, numberEnd))
            // 数字合法　开始数字动画
            start();
        else
            // 数字不合法　直接调用　setText　设置最终值
            setText(mPrefix + numberEnd + mPostfix);
    }

    public void setEnableAnim(boolean enableAnim) {
        isEnableAnim = enableAnim;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public void setPrefix(String mPrefixString) {
        this.mPrefix = mPrefixString;
    }

    public void setPostfix(String mPostfixString) {
        this.mPostfix = mPostfixString;
    }

    /**
     * 校验数字的合法性
     *
     * @param numberStart 　开始的数字
     * @param numberEnd   　结束的数字
     * @return 合法性
     */
    private boolean checkNumString(String numberStart, String numberEnd) {
        String regexInteger = "-?\\d*";
        isInt = numberEnd.matches(regexInteger) && numberStart.matches(regexInteger);
        if (isInt) {
            BigInteger start = new BigInteger(numberStart);
            BigInteger end = new BigInteger(numberEnd);
            return end.compareTo(start) >= 0;
        }
        String regexDecimal = "-?[1-9]\\d*.\\d*|-?0.\\d*[1-9]\\d*";
        if ("0".equals(numberStart)) {
            if (numberEnd.matches(regexDecimal)) {
                BigDecimal start = new BigDecimal(numberStart);
                BigDecimal end = new BigDecimal(numberEnd);
                return end.compareTo(start) > 0;
            }
        }
        if (numberEnd.matches(regexDecimal) && numberStart.matches(regexDecimal)) {
            BigDecimal start = new BigDecimal(numberStart);
            BigDecimal end = new BigDecimal(numberEnd);
            return end.compareTo(start) > 0;
        }
        return false;
    }

    private void start() {
        if (!isEnableAnim) {
            // 禁止动画
            setText(mPrefix + format(new BigDecimal(mNumEnd)) + mPostfix);
            return;
        }
        animator = ValueAnimator.ofObject(new BigDecimalEvaluator(), new BigDecimal(mNumStart), new BigDecimal(mNumEnd));
        animator.setDuration(mDuration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BigDecimal value = (BigDecimal) valueAnimator.getAnimatedValue();
                setText(mPrefix + format(value) + mPostfix);
            }
        });
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null)
            animator.cancel();
    }

    /**
     * 格式化 BigDecimal ,小数部分时保留两位小数并四舍五入
     *
     * @param bd 　BigDecimal
     * @return 格式化后的 String
     */
    private String format(BigDecimal bd) {
        StringBuilder pattern = new StringBuilder();
        if (isInt) {
            pattern.append("#,###");
        } else {
            int length = 0;
            String decimals = mNumEnd.split("\\.")[1];
            if (decimals != null) {
                length = decimals.length();
            }
            pattern.append("#,##0");
            if (length > 0) {
                pattern.append(".");
                for (int i = 0; i < length; i++) {
                    pattern.append("0");
                }
            }
        }
        DecimalFormat df = new DecimalFormat(pattern.toString());
        return df.format(bd);
    }

    private static class BigDecimalEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            BigDecimal start = (BigDecimal) startValue;
            BigDecimal end = (BigDecimal) endValue;
            BigDecimal result = end.subtract(start);
            return result.multiply(new BigDecimal("" + fraction)).add(start);
        }
    }

    @BindingAdapter(value = {"android:startNumber", "android:endNumber"}, requireAll = false)
    public static void setNumberText(NumberAnimTextView textView, String startNumber, String endNumber) {
        textView.setNumber(TextUtils.isEmpty(startNumber) ? "0" : startNumber,
                TextUtils.isEmpty(endNumber) ? "0" : endNumber);
    }

    @BindingAdapter(value = {"android:prefix", "android:postfix"}, requireAll = false)
    public static void setPrefixPostfix(NumberAnimTextView textView, String prefix, String postfix) {
        textView.setPrefix(TextUtils.isEmpty(prefix) ? "" : prefix);
        textView.setPostfix(TextUtils.isEmpty(postfix) ? "" : postfix);
    }
}
