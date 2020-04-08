package com.bulog.equote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RPKMap {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama_rpk")
    @Expose
    private String namaRpk;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("distance")
    @Expose
    private String distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaRpk() {
        return namaRpk;
    }

    public void setNamaRpk(String namaRpk) {
        this.namaRpk = namaRpk;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}