package com.topanlabs.unsoedpass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class kelasModel {
    @SerializedName("kodekelas")
    @Expose
    private String kodekelas;
    @SerializedName("namatkul")
    @Expose
    private String namatkul;
    @SerializedName("jam")
    @Expose
    private String jam;
    @SerializedName("ruangan")
    @Expose
    private String ruangan;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    public String getKodekelas() {
        return kodekelas;
    }

    public void setKodekelas(String kodekelas) {
        this.kodekelas = kodekelas;
    }

    public String getNamatkul() {
        return namatkul;
    }

    public void setNamatkul(String namatkul) {
        this.namatkul = namatkul;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getRuangan() {
        return ruangan;
    }

    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

}
