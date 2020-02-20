package com.topanlabs.unsoedpass.memo;

import android.app.Application;
import android.content.Context;

import com.topanlabs.unsoedpass.kelaspenggantidb.kelasdb;

import java.util.List;

public class memorepo {
    private memodao memoDAO;
    private List<memoent> memoDB;

    public memorepo(Context context) {
        kelasdb db = kelasdb.getDatabase(context);
        memoDAO  = db.memodao();

    }
    public List<memoent> getKelas () {
        return memoDAO.getAllKelas();
    }
    public void nukeTable() { memoDAO.nukeTable();}
    public Integer getCount() {return memoDAO.getCount();}
    public  List<memoent> getKelasIni( String hari) {
        return memoDAO.getTodayKelas(hari);
    }
    public void insert (memoent matkuldb) {
        memoDAO.insert(matkuldb);
    }
}
