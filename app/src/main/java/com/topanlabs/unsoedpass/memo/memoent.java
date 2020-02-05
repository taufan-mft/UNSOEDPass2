package com.topanlabs.unsoedpass.memo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "memo")
public class memoent {

    public int getId() {
        return id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;


    @NonNull
    @ColumnInfo(name = "namakul")
    private String namakul;
    @ColumnInfo(name = "jam")
    private String jam;
    @ColumnInfo(name = "ruangan")
    private String ruangan;
    @ColumnInfo(name = "tanggal")
    private String tanggal;
    @ColumnInfo(name = "catatan")
    private String catatan;
    @ColumnInfo(name = "jenis")
    private String jenis;
    @ColumnInfo(name = "idmemo")
    private Integer idmemo;

    public memoent(int id, String namakul, String jam, String ruangan, String tanggal, String catatan, String jenis, Integer idmemo) {
        this.id = id;
        this.namakul = namakul;
        this.jam = jam;
        this.idmemo = idmemo;
        this.tanggal = tanggal;
        this.ruangan = ruangan;
        this.catatan = catatan;
        this.jenis = jenis;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdmemo() {
        return idmemo;
    }

    public void setIdmemo(Integer idmemo) {
        this.idmemo = idmemo;
    }

    public void setNamakul(@NonNull String namakul) {
        this.namakul = namakul;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    @NonNull
    public String getNamakul() {
        return namakul;
    }

    public String getJam() {
        return jam;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getRuangan() {
        return ruangan;
    }
}
