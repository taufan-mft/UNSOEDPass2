package com.topanlabs.unsoedpass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class tokenmodel {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("password")
    @Expose
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String username) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public tokenmodel(String username, String password) {
        this.username= username;
        this.password = password;

    }
}