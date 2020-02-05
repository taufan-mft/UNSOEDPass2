package com.topanlabs.unsoedpass.memo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class memoModel {
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
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("jenis")
    @Expose
    private String jenis;
    @SerializedName("catatan")
    @Expose
    private String catatan;
    @SerializedName("idmemo")
    @Expose
    private Integer idmemo;
    public memoModel(String kodekelas, String namatkul, String jam, String ruangan, String tanggal, Integer id, String jenis, String catatan, Integer idmemo) {
        this.kodekelas = kodekelas;
        this.namatkul = namatkul;
        this.jam = jam;
        this.ruangan = ruangan;
        this.tanggal = tanggal;
        this.id = id;
        this.idmemo = idmemo;
        this.jenis = jenis;
        this.catatan = catatan;
    }

    public String getKodekelas() {
        return kodekelas;
    }

    public void setKodekelas(String kodekelas) {
        this.kodekelas = kodekelas;
    }

    public Integer getIdmemo() {
        return idmemo;
    }

    public void setIdmemo(Integer idmemo) {
        this.idmemo = idmemo;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
