package com.topanlabs.unsoedpass;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface matkulDAO {
    @Query("SELECT * FROM matkuldb")
    LiveData<List<matkuldb>> getAll();

    @Insert
    void insert(matkuldb matkuldb);


}
