package com.topanlabs.unsoedpass.kelaspenggantidb;

import android.app.Application;

import java.util.List;

public class kelasRepository {
    private kelaspengDAO kelasDAO;
    private List<kelaspengganti> kelasDB;

   public kelasRepository(Application application) {
        kelasdb db = kelasdb.getDatabase(application);
        kelasDAO = db.kelaspengDAO();

    }
    public List<kelaspengganti> getKelas () {
        return kelasDAO.getAllKelas();
    }
    public void nukeTable() { kelasDAO.nukeTable();}
    public  List<kelaspengganti> getKelasIni( String hari) {
        return kelasDAO.getTodayKelas(hari);
    }
    public void insert (kelaspengganti matkuldb) {
        kelasDAO.insert(matkuldb);
    }
}
