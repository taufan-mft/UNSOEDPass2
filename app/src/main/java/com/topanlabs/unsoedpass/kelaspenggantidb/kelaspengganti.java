package com.topanlabs.unsoedpass.kelaspenggantidb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "kelaspengganti")
public class kelaspengganti {

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

    public kelaspengganti(int id, String namakul, String jam, String ruangan, String tanggal) {
        this.id = id;
        this.namakul = namakul;
        this.jam = jam;
        this.tanggal = tanggal;
        this.ruangan = ruangan;
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
