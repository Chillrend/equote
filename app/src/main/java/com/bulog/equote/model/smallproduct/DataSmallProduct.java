package com.bulog.equote.model.smallproduct;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DataSmallProduct implements Parcelable {
    @SerializedName("Category")
    @Expose
    private String category;

    @SerializedName("Product")
    @Expose
    private ArrayList<SmallProduct> products = null;

    public DataSmallProduct(String category, ArrayList<SmallProduct> products) {
        this.category = category;
        this.products = products;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<SmallProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<SmallProduct> products) {
        this.products = products;
    }

    protected DataSmallProduct(Parcel in){
        category = in.readString();
        products = new ArrayList<SmallProduct>();
        in.readList(products, SmallProduct.class.getClassLoader());
    }

    public static final Creator<DataSmallProduct> CREATOR = new Creator<DataSmallProduct>() {
        @Override
        public DataSmallProduct createFromParcel(Parcel source) {
            return new DataSmallProduct(source);
        }

        @Override
        public DataSmallProduct[] newArray(int size) {
            return new DataSmallProduct[0];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(category);
        dest.writeList(products);
    }
}
