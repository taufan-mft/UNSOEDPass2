package com.topanlabs.unsoedpass;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class checkoutmodel {

    @SerializedName("NIM")
    @Expose
    private String nIM;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("jumlah")
    @Expose
    private String jumlah;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("jam")
    @Expose
    private String jam;

    public checkoutmodel(String nIM, String tanggal, String bank, String jumlah, String status, String jam) {
        this.nIM= nIM;
        this.tanggal = tanggal;
        this.bank = bank;
        this.jumlah = jumlah;
        this.status = status;
        this.jam = jam;

    }

    public String getNIM() {
        return nIM;
    }

    public void setNIM(String nIM) {
        this.nIM = nIM;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

}