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

    public memoent(int id, String namakul, String jam, String ruangan, String tanggal, String catatan, String jenis) {
        this.id = id;
        this.namakul = namakul;
        this.jam = jam;
        this.tanggal = tanggal;
        this.ruangan = ruangan;
        this.catatan = catatan;
        this.jenis = jenis;
    }

    public void setId(int id) {
        this.id = id;
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
