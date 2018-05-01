package com.sunnybear.library.third.wechat.entity;

import java.io.Serializable;

/**
 * <p>
 * Created by chenkai.gu on 2018/3/20.
 */
public class WXBaseResp implements Serializable {
    /**
     * code : 0712DRee0kzwiz13fUce0900feRet
     * country : CN
     * errCode : 0
     * lang : zh_CN
     * state : wechat_sdk_微信登录
     * type : 1
     * url : wxb363a9ff53731258://oauth?code=0712DRee0kzwiz13fUce0900fe02DRet&ate=wechat_sdk_%E5%BE%AE%E4%BF%A1%E7%99%BB%E5%BD%95
     */

    private String code;
    private String country;
    private int errCode;
    private String lang;
    private String state;
    private int type;
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WXBaseResp{" +
                "code='" + code + '\'' +
                ", country='" + country + '\'' +
                ", errCode=" + errCode +
                ", lang='" + lang + '\'' +
                ", state='" + state + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                '}';
    }
}
