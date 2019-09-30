package com.topanlabs.unsoedpass;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "matkuldb")
public class matkuldb {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "namakul")
    private String namakul;
    @ColumnInfo(name = "hari")
    private String hari;
    @ColumnInfo(name = "ruangan")
    private String ruangan;
    @ColumnInfo(name = "jam")
    private String jam;

    public matkuldb(String namakul, String hari, String ruangan, String jam) {
        this.namakul = namakul;
        this.hari = hari;
        this.ruangan = ruangan;
        this.jam = jam;
    }

    public String getNamakul() {
        return namakul;
    }

    public String getHari() {
        return hari;
    }

    public String getRuangan() {
        return ruangan;
    }

    public String getJam() {
        return jam;
    }

    public void setNamakul(String namakul) {
        this.namakul = namakul;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
