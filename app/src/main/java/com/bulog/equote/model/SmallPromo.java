package com.bulog.equote.model;

import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SmallPromo implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("promo_title")
    @Expose
    private String promoTitle;
    @SerializedName("image")
    @Expose
    private String image;

    public SmallPromo(Integer id, String promoTitle, String image) {
        this.id = id;
        this.promoTitle = promoTitle;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoTitle() {
        return promoTitle;
    }

    public void setPromoTitle(String promoTitle) {
        this.promoTitle = promoTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
