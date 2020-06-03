package com.bulog.equote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Promo {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("promo_title")
    @Expose
    private String promoTitle;
    @SerializedName("promo_desc")
    @Expose
    private String promoDesc;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("promo_start_date")
    @Expose
    private String promoStartDate;
    @SerializedName("promo_end_date")
    @Expose
    private String promoEndDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public String getPromoDesc() {
        return promoDesc;
    }

    public void setPromoDesc(String promoDesc) {
        this.promoDesc = promoDesc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPromoStartDate() {
        return promoStartDate;
    }

    public void setPromoStartDate(String promoStartDate) {
        this.promoStartDate = promoStartDate;
    }

    public String getPromoEndDate() {
        return promoEndDate;
    }

    public void setPromoEndDate(String promoEndDate) {
        this.promoEndDate = promoEndDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
