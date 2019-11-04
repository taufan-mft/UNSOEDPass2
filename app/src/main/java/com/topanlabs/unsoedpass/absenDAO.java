package com.topanlabs.unsoedpass;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface absenDAO {
    @Query("SELECT * FROM absendb WHERE namakul = :hari")
    List<absendb> getDetailAbsen(String hari);
    @Query("DELETE FROM absendb")
    public void nukeTable();
    @Insert
    void insert(absendb absendb);
}
