package com.topanlabs.unsoedpass.memo;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.topanlabs.unsoedpass.absendb;

import java.util.List;
@Dao
public interface memodao {
    @Query("SELECT * FROM memo")
    List<memoent> getAllKelas();
    @Query("DELETE FROM memo")
    public void nukeTable();
    @Insert
    void insert(memoent kelaspengganti);
    @Query("SELECT COUNT(*) FROM memo")
    Integer getCount();
    @Query("SELECT * FROM memo WHERE tanggal = :hari")
    List<memoent> getTodayKelas(String hari);

}
