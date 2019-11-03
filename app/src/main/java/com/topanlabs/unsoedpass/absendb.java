package com.topanlabs.unsoedpass;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "absendb")
public class absendb {

    public int getId() {
        return id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;


    @NonNull
    @ColumnInfo(name = "namakul")
    private String namakul;
    @ColumnInfo(name = "hari")
    private String hari;
    @ColumnInfo(name = "tanggal")
    private String tanggal;
    @ColumnInfo(name = "kehadiran")
    private String kehadiran;

    public absendb(int id, String namakul, String hari, String tanggal, String kehadiran) {
        this.id = id;
        this.namakul = namakul;
        this.hari = hari;
        this.tanggal = tanggal;
        this.kehadiran = kehadiran;
    }

    @NonNull
    public String getNamakul() {
        return namakul;
    }

    public String getHari() {
        return hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKehadiran() {
        return kehadiran;
    }
}
