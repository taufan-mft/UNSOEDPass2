package com.topanlabs.unsoedpass;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface absenDAO {
    @Query("SELECT * FROM absendb WHERE namakul = :hari")
    List<absendb> getDetailAbsen(String hari);

    @Insert
    void insert(absendb absendb);
}
