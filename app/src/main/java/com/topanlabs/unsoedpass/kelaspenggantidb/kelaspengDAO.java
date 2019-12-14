package com.topanlabs.unsoedpass.kelaspenggantidb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.topanlabs.unsoedpass.absendb;

import java.util.List;
@Dao
public interface kelaspengDAO {
    @Query("SELECT * FROM kelaspengganti")
    List<kelaspengganti> getAllKelas();
    @Query("DELETE FROM kelaspengganti")
    public void nukeTable();
    @Insert
    void insert(kelaspengganti kelaspengganti);
}
