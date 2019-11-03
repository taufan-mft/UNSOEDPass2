package com.topanlabs.unsoedpass;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class matkulRepository {
    private matkulDAO matkulDao;
    private LiveData<List<matkuldb>> matkulDB;

    matkulRepository(Application application) {
        matkulDatabase db = matkulDatabase.getDatabase(application);
        matkulDao = db.matkulDao();
        matkulDB = matkulDao.getAll();
    }

    LiveData<List<matkuldb>> getAll() {
        return matkulDB;
    }
    public LiveData<Integer> getCount() {
        return matkulDao.getCount();
    }

    public Integer getCount2() {
        return matkulDao.getCount2();
    }

    public List<matkuldb> getTodayMat (String hari) {
        return matkulDao.getTodayMatk(hari);
    }

    public List<matkuldb> getAllMat() { return matkulDao.getAllMat();}

    public void insert (matkuldb matkuldb) {
        new insertAsyncTask(matkulDao).execute(matkuldb);
    }
    public void ambilCount () {}

    private static class insertAsyncTask extends AsyncTask<matkuldb, Void, Void> {

        private matkulDAO mAsyncTaskDao;

        insertAsyncTask(matkulDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final matkuldb... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }









}
