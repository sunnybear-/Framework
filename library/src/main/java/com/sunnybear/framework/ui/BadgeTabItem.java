package com.sunnybear.framework.ui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.sunnybear.framework.BR;

/**
 * 红点TabItem
 * Created by chenkai.gu on 2018/5/18.
 */
public class BadgeTabItem extends BaseObservable {

    private int badgeItemLayoutId;
    //红点信息
    private String badgeText;
    //是否选中
    private boolean select;
    //是否有提示红点
    private boolean badge;

    public BadgeTabItem() {
    }

    public BadgeTabItem(int badgeItemLayoutId) {
        this.badgeItemLayoutId = badgeItemLayoutId;
    }

    public BadgeTabItem(int badgeItemLayoutId, boolean select) {
        this.badgeItemLayoutId = badgeItemLayoutId;
        this.select = select;
    }

    public BadgeTabItem(int badgeItemLayoutId, String badgeText, boolean select, boolean badge) {
        this.badgeItemLayoutId = badgeItemLayoutId;
        this.badgeText = badgeText;
        this.select = select;
        this.badge = badge;
    }

    @Bindable
    public int getBadgeItemLayoutId() {
        return badgeItemLayoutId;
    }

    public void setBadgeItemLayoutId(int badgeItemLayoutId) {
        this.badgeItemLayoutId = badgeItemLayoutId;
        notifyPropertyChanged(BR.badgeItemLayoutId);
    }

    @Bindable
    public String getBadgeText() {
        return badgeText;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
        notifyPropertyChanged(BR.badgeText);
    }

    @Bindable
    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        notifyPropertyChanged(BR.select);
    }

    @Bindable
    public boolean isBadge() {
        return badge;
    }

    public void setBadge(boolean badge) {
        this.badge = badge;
        notifyPropertyChanged(BR.badge);
    }

    @Override
    public String toString() {
        return "BadgeTabItem{" +
                "badgeItemLayoutId=" + badgeItemLayoutId +
                ", badgeText='" + badgeText + '\'' +
                ", select=" + select +
                ", badge=" + badge +
                '}';
    }
}
