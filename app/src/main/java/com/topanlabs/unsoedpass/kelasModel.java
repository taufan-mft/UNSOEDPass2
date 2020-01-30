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
    @SerializedName("ketuakelas")
    @Expose
    private String ketuakelas;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("id")
    @Expose
    private String id;

    public kelasModel(String kodekelas, String ketuakelas, String created){
        this.kodekelas = kodekelas;
        this.ketuakelas = ketuakelas;
        this.created = created;
    }
    public kelasModel(String kodekelas, String namatkul, String jam, String ruangan, String tanggal){
        this.kodekelas = kodekelas;
        this.namatkul = namatkul;
        this.jam = jam;
        this.ruangan = ruangan;
        this.tanggal = tanggal;
       
    }
    public kelasModel(String kodekelas, String namatkul, String jam, String ruangan, String tanggal, String id){
        this.kodekelas = kodekelas;
        this.namatkul = namatkul;
        this.jam = jam;
        this.ruangan = ruangan;
        this.tanggal = tanggal;
        this.id = id;
    }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getKetuakelas() {
        return ketuakelas;
    }

    public void setKetuakelas(String ketuakelas) {
        this.ketuakelas = ketuakelas;
    }
}
