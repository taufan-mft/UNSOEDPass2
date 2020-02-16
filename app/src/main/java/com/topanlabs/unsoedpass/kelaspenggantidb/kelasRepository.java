package com.topanlabs.unsoedpass.kelaspenggantidb;

import android.app.Application;
import android.content.Context;

import java.util.List;

public class kelasRepository {
    private kelaspengDAO kelasDAO;
    private List<kelaspengganti> kelasDB;

   public kelasRepository(Context context) {
        kelasdb db = kelasdb.getDatabase(context);
        kelasDAO = db.kelaspengDAO();

    }
    public List<kelaspengganti> getKelas () {
        return kelasDAO.getAllKelas();
    }
    public void nukeTable() { kelasDAO.nukeTable();}
    public  List<kelaspengganti> getKelasIni( String hari) {
        return kelasDAO.getTodayKelas(hari);
    }
    public Integer getCount() {return kelasDAO.getCount2();}
    public void insert (kelaspengganti matkuldb) {
        kelasDAO.insert(matkuldb);
    }
}
