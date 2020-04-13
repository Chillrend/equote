package com.bulog.equote.model.smallproduct;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmallProduct implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("product_name")
    @Expose
    private String productName;

    @SerializedName("short_desc")
    @Expose
    private String shortDesc;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("image")
    @Expose
    private String imageUrl;

    @SerializedName("category")
    @Expose
    private String category;

    public SmallProduct(String id, String productName, String shortDesc, String color, String imageUrl, String category) {
        this.id = id;
        this.productName = productName;
        this.shortDesc = shortDesc;
        this.color = color;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    protected SmallProduct (Parcel in){
        id = in.readString();
        productName = in.readString();
        shortDesc = in.readString();
        color = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<SmallProduct> CREATOR = new Creator<SmallProduct>() {
        @Override
        public SmallProduct createFromParcel(Parcel source) {
            return new SmallProduct(source);
        }

        @Override
        public SmallProduct[] newArray(int size) {
            return new SmallProduct[0];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(id);
        dest.writeString(productName);
        dest.writeString(shortDesc);
        dest.writeString(color);
        dest.writeString(imageUrl);
    }
}

