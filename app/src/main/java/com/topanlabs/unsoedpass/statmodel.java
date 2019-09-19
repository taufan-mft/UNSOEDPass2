package com.topanlabs.unsoedpass;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class statmodel {

    @SerializedName("NIM")
    @Expose
    private String nIM;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("lastseen")
    @Expose
    private String lastseen;
    @SerializedName("devicename")
    @Expose
    private String devicename;
    public statmodel(String nIM, String brand, String lastseen, String devicename) {
        this.nIM= nIM;
        this.lastseen = lastseen;
        this.brand = brand;
        this.devicename = devicename;

    }


    public String getNIM() {
        return nIM;
    }

    public void setNIM(String nIM) {
        this.nIM = nIM;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

}