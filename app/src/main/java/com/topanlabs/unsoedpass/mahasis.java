package com.topanlabs.unsoedpass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class mahasis {


    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("NIM")
    @Expose
    private String nIM;
    @SerializedName("beli")
    @Expose
    private String beli;
    @SerializedName("noorder")
    @Expose
    private String noorder;
    @SerializedName("tawal")
    @Expose
    private String tawal;
    @SerializedName("fakultas")
    @Expose
    private String fakultas;
    @SerializedName("jurusan")
    @Expose
    private String jurusan;
    @SerializedName("kodekelas")
    @Expose
    private String kodekelas;
    @SerializedName("raisa")
    @Expose
    private String raisa;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public mahasis(String nIM, String tawal, String fakultas, String jurusan, String nama, String beli, String noorder, String kodekelas, String raisa ) {
        this.nIM = nIM;
        this.tawal = tawal;
        this.fakultas = fakultas;
        this.jurusan = jurusan;
        this.nama = nama;
        this.beli = beli;
        this.noorder= noorder;
        this.raisa = raisa;
        this.kodekelas = kodekelas;
    }



    public String getNama() {
        return nama;
    }


    public String getNIM() {
        return nIM;
    }

    public void setNIM(String nIM) {
        this.nIM = nIM;
    }

    public String getBeli() {
        return beli;
    }

    public void setBeli(String beli) {
        this.beli = beli;
    }

    public String getNoorder() {
        return noorder;
    }

    public void setNoorder(String nIM) {
        this.noorder = noorder;
    }

    public String getTawal() {
        return tawal;
    }

    public void setTawal(String tawal) {
        this.tawal = tawal;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getRaisa() {
        return raisa;
    }

    public void setRaisa(String raisa) {
        this.raisa = raisa;
    }

    public String getKodekelas() {
        return kodekelas;
    }

    public void setKodekelas(String kodekelas) {
        this.kodekelas = kodekelas;
    }
}