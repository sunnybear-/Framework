/**
 * //┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * ////┃　　　┃   神兽保佑
 * ////┃　　　┃   代码无BUG！
 * ////┃　　　┗━━━┓
 * ////┃　　　　　　　┣┓
 * ////┃　　　　　　　┏┛
 * ////┗┓┓┏━┳┓┏┛
 * //////┃┫┫　┃┫┫
 * //////┗┻┛　┗┻┛
 */
package com.sunnybear.framework.ui.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunnybear.framework.R;
import com.sunnybear.framework.tools.StringUtils;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

/**
 * 封装Dialog
 * Created by chenkai.gu on 2017/3/22.
 */
public class FancyAlertDialog extends DialogFragment {
    private static final String TAG = FancyAlertDialog.class.getSimpleName();
    private Builder mBuilder;

    private static FancyAlertDialog instance = new FancyAlertDialog();

    private static FancyAlertDialog getInstance() {
        return instance;
    }

    public static FancyAlertDialog.Builder getDialogBuilder(AppCompatActivity activity) {
        return new Builder(activity);
    }

    private CardView mCardView;
    private AppCompatImageView mImage;
    private TextView mTitle, mSubTitle, mBody;
    private AppCompatButton mPositive, mNegative;
    private LinearLayout mButtonsPanel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            mBuilder = (Builder) savedInstanceState.getSerializable(Builder.class.getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Builder.class.getSimpleName(), mBuilder);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_alert, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (mBuilder.getAnimation() != 0)
            dialog.getWindow().getAttributes().windowAnimations = mBuilder.animation;
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        if (mBuilder != null) {
            if (!StringUtils.isEmpty(mBuilder.getTextTitle())) {
                mTitle.setText(mBuilder.getTextTitle());
                if (mBuilder.getTitleColor() != 0)
                    mTitle.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getTitleColor()));
            } else if (mBuilder.getTextTitleResId() != 0) {
                mTitle.setText(getActivity().getResources().getString(mBuilder.getTextTitleResId()));
                if (mBuilder.getTitleColor() != 0)
                    mTitle.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getTitleColor()));
            } else {
                mTitle.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty(mBuilder.getTextSubTitle())) {
                mSubTitle.setText(mBuilder.getTextSubTitle());
                if (mBuilder.getSubtitleColor() != 0)
                    mSubTitle.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getSubtitleColor()));
            } else if (mBuilder.getTextSubTitleResId() != 0) {
                mSubTitle.setText(getActivity().getResources().getString(mBuilder.getTextSubTitleResId()));
                if (mBuilder.getSubtitleColor() != 0)
                    mSubTitle.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getSubtitleColor()));
            } else {
                mSubTitle.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty(mBuilder.getBody())) {
                mBody.setText(mBuilder.getBody());
                if (mBuilder.getBodyColor() != 0)
                    mBody.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getBodyColor()));
            } else if (mBuilder.getBodyResId() != 0) {
                mBody.setText(getActivity().getResources().getString(mBuilder.getBodyResId()));
                if (mBuilder.getBodyColor() != 0)
                    mBody.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getBodyColor()));
            } else {
                mBody.setVisibility(View.GONE);
            }

            if (!StringUtils.isEmpty(mBuilder.getPositiveButtonText())) {
                mPositive.setText(mBuilder.getPositiveButtonText());
                if (mBuilder.getPositiveTextColor() != 0)
                    mPositive.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getPositiveTextColor()));
                if (mBuilder.getOnPositiveClickListener() != null)
                    mPositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBuilder.getOnPositiveClickListener().onClick(v, getDialog());
                        }
                    });
            } else if (mBuilder.getPositiveButtonTextResId() != 0) {
                mPositive.setText(getActivity().getResources().getString(mBuilder.getPositiveButtonTextResId()));
                if (mBuilder.getPositiveTextColor() != 0)
                    mPositive.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getPositiveTextColor()));
                if (mBuilder.getOnPositiveClickListener() != null)
                    mPositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBuilder.getOnPositiveClickListener().onClick(v, getDialog());
                        }
                    });
            } else {
                mPositive.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty(mBuilder.getNegativeButtonText())) {
                mNegative.setText(mBuilder.getNegativeButtonText());
                if (mBuilder.getNegativeTextColor() != 0)
                    mNegative.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getNegativeTextColor()));
                if (mBuilder.getOnNegativeClickListener() != null)
                    mNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBuilder.getOnNegativeClickListener().onClick(v, getDialog());
                        }
                    });
            } else if (mBuilder.getNegativeButtonTextResId() != 0) {
                mNegative.setText(getActivity().getResources().getString(mBuilder.getNegativeButtonTextResId()));
                if (mBuilder.getNegativeTextColor() != 0)
                    mNegative.setTextColor(ContextCompat.getColor(getActivity(), mBuilder.getNegativeTextColor()));
                if (mBuilder.getOnNegativeClickListener() != null)
                    mNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBuilder.getOnNegativeClickListener().onClick(v, getDialog());
                        }
                    });
            } else {
                mNegative.setVisibility(View.GONE);
            }

            if (mBuilder.getImageRecourse() != 0) {
                Drawable imageRes = VectorDrawableCompat.create(getResources(), mBuilder.getImageRecourse(), getActivity().getTheme());
                mImage.setImageDrawable(imageRes);
            } else if (mBuilder.getImageDrawable() != null) {
                mImage.setImageDrawable(mBuilder.getImageDrawable());
            } else {
                mImage.setVisibility(View.GONE);
            }

            if (mBuilder.getBackgroundColor() != 0)
                mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), mBuilder.getBackgroundColor()));

            if (mBuilder.isAutoHide()) {
                int time = mBuilder.getTimeToHide() != 0 ? mBuilder.getTimeToHide() : 10 * 1000;
                Flowable.timer(time, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (isAdded() && getActivity() != null) dismiss();
                            }
                        }).subscribe();
            }

            if (mBuilder.getTitleFont() != null)
                mTitle.setTypeface(mBuilder.getTitleFont());
            if (mBuilder.getSubTitleFont() != null)
                mSubTitle.setTypeface(mBuilder.getSubTitleFont());
            if (mBuilder.getBodyFont() != null)
                mBody.setTypeface(mBuilder.getBodyFont());
            if (mBuilder.getPositiveButtonFont() != null)
                mPositive.setTypeface(mBuilder.getPositiveButtonFont());
            if (mBuilder.getNegativeButtonFont() != null)
                mNegative.setTypeface(mBuilder.getNegativeButtonFont());
            if (mBuilder.getAlertFont() != null) {
                mTitle.setTypeface(mBuilder.getAlertFont());
                mSubTitle.setTypeface(mBuilder.getAlertFont());
                mBody.setTypeface(mBuilder.getAlertFont());
                mPositive.setTypeface(mBuilder.getAlertFont());
                mNegative.setTypeface(mBuilder.getAlertFont());
            }

            if (mBuilder.getButtonsGravity() != null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                switch (mBuilder.getButtonsGravity()) {
                    case LEFT:
                        params.gravity = Gravity.LEFT;
                        break;
                    case RIGHT:
                        params.gravity = Gravity.RIGHT;
                        break;
                    case CENTER:
                        params.gravity = Gravity.CENTER;
                        break;
                }
                params.bottomMargin = 40;
                params.topMargin = 100;
                params.leftMargin = 40;
                params.rightMargin = 40;
                mButtonsPanel.setLayoutParams(params);
            }
        }
    }

    private void initViews(View view) {
        mCardView = (CardView) view.findViewById(R.id.card_view);
        mImage = (AppCompatImageView) view.findViewById(R.id.image);
        mTitle = (TextView) view.findViewById(R.id.title);
        mSubTitle = (TextView) view.findViewById(R.id.sub_title);
        mBody = (TextView) view.findViewById(R.id.body);
        mPositive = (AppCompatButton) view.findViewById(R.id.position);
        mNegative = (AppCompatButton) view.findViewById(R.id.negative);
        mButtonsPanel = (LinearLayout) view.findViewById(R.id.buttons_panel);
    }

    private Dialog show(AppCompatActivity activity, Builder builder) {
        this.mBuilder = builder;
        show(activity.getSupportFragmentManager(), TAG);
        return getDialog();
    }

    /**
     * FancyAlert建造器
     * Created by chenkai.gu on 2017/3/22.
     */
    public static class Builder implements Serializable {
        private String positiveButtonText;
        private String negativeButtonText;
        private String textTitle;
        private String textSubTitle;
        private String body;

        private int positiveButtonTextResId;
        private int negativeButtonTextResId;
        private int textTitleResId;
        private int textSubTitleResId;
        private int bodyResId;

        private int animation;

        private OnPositiveClickListener mOnPositiveClickListener;
        private OnNegativeClickListener mOnNegativeClickListener;

        private boolean autoHide;
        private int timeToHide;

        private int positiveTextColor;
        private int backgroundColor;
        private int negativeTextColor;
        private int titleColor;
        private int subtitleColor;
        private int bodyColor;

        private int imageRecourse;
        private Drawable imageDrawable;

        private Typeface titleFont;
        private Typeface subTitleFont;
        private Typeface bodyFont;
        private Typeface positiveButtonFont;
        private Typeface negativeButtonFont;
        private Typeface alertFont;

        private AppCompatActivity mActivity;

        private PanelGravity buttonsGravity;

        private Builder(AppCompatActivity activity) {
            mActivity = activity;
        }

        private int getAnimation() {
            return animation;
        }

        private int getPositiveButtonTextResId() {
            return positiveButtonTextResId;
        }

        private int getNegativeButtonTextResId() {
            return negativeButtonTextResId;
        }

        private int getTextTitleResId() {
            return textTitleResId;
        }

        private int getTextSubTitleResId() {
            return textSubTitleResId;
        }

        private int getBodyResId() {
            return bodyResId;
        }

        public Builder animation(int animation) {
            this.animation = animation;
            return this;
        }

        private int getTitleColor() {
            return titleColor;
        }

        public Builder titleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        private String getPositiveButtonText() {
            return positiveButtonText;
        }

        public Builder positiveButtonText(String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        public Builder positiveButtonText(int positiveButtonTextResId) {
            this.positiveButtonTextResId = positiveButtonTextResId;
            return this;
        }

        private String getNegativeButtonText() {
            return negativeButtonText;
        }

        public Builder negativeButtonText(String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public Builder negativeButtonText(int negativeButtonTextResId) {
            this.negativeButtonTextResId = negativeButtonTextResId;
            return this;
        }

        private String getTextTitle() {
            return textTitle;
        }

        public Builder title(String textTitle) {
            this.textTitle = textTitle;
            return this;
        }

        public Builder title(int textTitleResId) {
            this.textTitleResId = textTitleResId;
            return this;
        }

        private String getTextSubTitle() {
            return textSubTitle;
        }

        public Builder subTitle(String textSubTitle) {
            this.textSubTitle = textSubTitle;
            return this;
        }

        public Builder subTitle(int textSubTitleResId) {
            this.textSubTitleResId = textSubTitleResId;
            return this;
        }

        private String getBody() {
            return body;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder body(int bodyResId) {
            this.bodyResId = bodyResId;
            return this;
        }

        private OnPositiveClickListener getOnPositiveClickListener() {
            return mOnPositiveClickListener;
        }

        public Builder setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
            mOnPositiveClickListener = onPositiveClickListener;
            return this;
        }

        private OnNegativeClickListener getOnNegativeClickListener() {
            return mOnNegativeClickListener;
        }

        public Builder setOnNegativeClickListener(OnNegativeClickListener onNegativeClickListener) {
            mOnNegativeClickListener = onNegativeClickListener;
            return this;
        }

        private boolean isAutoHide() {
            return autoHide;
        }

        public Builder autoHide(boolean autoHide) {
            this.autoHide = autoHide;
            return this;
        }

        private int getTimeToHide() {
            return timeToHide;
        }

        public Builder timeToHide(int timeToHide) {
            this.timeToHide = timeToHide;
            return this;
        }

        private int getPositiveTextColor() {
            return positiveTextColor;
        }

        public Builder positiveTextColor(int positiveTextColor) {
            this.positiveTextColor = positiveTextColor;
            return this;
        }

        private int getBackgroundColor() {
            return backgroundColor;
        }

        public Builder backgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        private int getNegativeTextColor() {
            return negativeTextColor;
        }

        public Builder negativeTextColor(int negativeTextColor) {
            this.negativeTextColor = negativeTextColor;
            return this;
        }

        private int getSubtitleColor() {
            return subtitleColor;
        }

        public Builder subtitleColor(int subtitleColor) {
            this.subtitleColor = subtitleColor;
            return this;
        }

        private int getBodyColor() {
            return bodyColor;
        }

        public Builder bodyColor(int bodyColor) {
            this.bodyColor = bodyColor;
            return this;
        }

        private int getImageRecourse() {
            return imageRecourse;
        }

        public Builder imageRecourse(int imageRecourse) {
            this.imageRecourse = imageRecourse;
            return this;
        }

        private Drawable getImageDrawable() {
            return imageDrawable;
        }

        public Builder imageDrawable(Drawable imageDrawable) {
            this.imageDrawable = imageDrawable;
            return this;
        }

        private Typeface getTitleFont() {
            return titleFont;
        }

        public Builder titleFont(Typeface titleFont) {
            this.titleFont = titleFont;
            return this;
        }

        private Typeface getSubTitleFont() {
            return subTitleFont;
        }

        public Builder subTitleFont(Typeface subTitleFont) {
            this.subTitleFont = subTitleFont;
            return this;
        }

        private Typeface getBodyFont() {
            return bodyFont;
        }

        public Builder bodyFont(Typeface bodyFont) {
            this.bodyFont = bodyFont;
            return this;
        }

        private Typeface getPositiveButtonFont() {
            return positiveButtonFont;
        }

        public Builder positiveButtonFont(Typeface positiveButtonFont) {
            this.positiveButtonFont = positiveButtonFont;
            return this;
        }

        private Typeface getNegativeButtonFont() {
            return negativeButtonFont;
        }

        public Builder negativeButtonFont(Typeface negativeButtonFont) {
            this.negativeButtonFont = negativeButtonFont;
            return this;
        }

        private Typeface getAlertFont() {
            return alertFont;
        }

        public Builder alertFont(Typeface alertFont) {
            this.alertFont = alertFont;
            return this;
        }

        private PanelGravity getButtonsGravity() {
            return buttonsGravity;
        }

        public Builder buttonsGravity(PanelGravity buttonsGravity) {
            this.buttonsGravity = buttonsGravity;
            return this;
        }

        public Builder build() {
            return this;
        }

        public Dialog show() {
            return FancyAlertDialog.getInstance().show(mActivity, this);
        }
    }

    /**
     * Created by chenkai.gu on 2017/3/22.
     */
    public interface OnPositiveClickListener {

        void onClick(View v, Dialog dialog);
    }

    /**
     * Created by chenkai.gu on 2017/3/22.
     */
    public interface OnNegativeClickListener {

        void onClick(View v, Dialog dialog);
    }

    /**
     * Created by chenkai.gu on 2017/3/22.
     */
    public enum PanelGravity {
        LEFT, RIGHT, CENTER
    }
}
