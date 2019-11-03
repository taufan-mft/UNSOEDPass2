package com.topanlabs.unsoedpass;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class absenRepository {
    private absenDAO absenDAO;
    private List<absendb> absenDB;

    absenRepository(Application application) {
        absenDatabase db = absenDatabase.getDatabase(application);
        absenDAO = db.absenDAO();

    }
    public List<absendb> getDetailAbsen (String hari) {
        return absenDAO.getDetailAbsen(hari);
    }

    public void insert (absendb matkuldb) {
        absenDAO.insert(matkuldb);
    }
}
