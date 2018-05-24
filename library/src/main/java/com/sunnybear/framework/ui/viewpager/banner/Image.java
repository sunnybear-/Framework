package com.sunnybear.framework.ui.viewpager.banner;

import java.io.Serializable;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/24.
 */
public class Image implements Serializable {
    //图片标题
    private String title;
    //图片url
    private Object imageUrl;

    public Image() {
    }

    public Image(String title, Object imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Image{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
