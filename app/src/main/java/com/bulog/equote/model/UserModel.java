
package com.bulog.equote.model;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserModel {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("fullname")
    private String mFullname;
    @SerializedName("id_role")
    private Long mIdRole;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("token")
    private String mToken;
    @SerializedName("is_social_user")
    private boolean socialUser;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFullname() {
        return mFullname;
    }

    public void setFullname(String fullname) {
        mFullname = fullname;
    }

    public Long getIdRole() {
        return mIdRole;
    }

    public void setIdRole(Long idRole) {
        mIdRole = idRole;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public boolean isSocialUser() {
        return socialUser;
    }

    public void setSocialUser(boolean socialUser) {
        this.socialUser = socialUser;
    }
}
