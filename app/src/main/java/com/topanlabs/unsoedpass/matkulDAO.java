package com.topanlabs.unsoedpass;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface matkulDAO {
    @Query("SELECT * FROM matkuldb ORDER BY CASE hari when 'SENIN' then 0 when 'SELASA' then 1 when 'RABU' then 2 when 'KAMIS' then 3 when 'JUMAT' then 4 end, jam")
    LiveData<List<matkuldb>> getAll();

    @Query("SELECT COUNT(namakul) FROM matkuldb")
    LiveData<Integer> getCount();

    @Query("SELECT COUNT(*) FROM matkuldb")
    Integer getCount2();

    @Query("SELECT * FROM matkuldb WHERE hari = :hari")
    List<matkuldb> getTodayMatk(String hari);

    @Query("SELECT * FROM matkuldb")
    List<matkuldb> getAllMat();

    @Insert
    void insert(matkuldb matkuldb);


}
