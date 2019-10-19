package com.topanlabs.unsoedpass;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class matkulViewModel extends AndroidViewModel {
    private matkulRepository mRepository;
    private LiveData<List<matkuldb>> allMatkul;
    private LiveData<Integer> count;

    public matkulViewModel (Application application) {
        super(application);
        mRepository = new matkulRepository(application);
        allMatkul = mRepository.getAll();
        count = mRepository.getCount();
    }

    LiveData<List<matkuldb>> getAll() {

            allMatkul = mRepository.getAll();

         return allMatkul; }
    public LiveData<Integer> getCount() {
        if (count == null){
            count = mRepository.getCount();
        }
        return mRepository.getCount(); }

    public void insert(matkuldb matkuldb) { mRepository.insert(matkuldb); }




}
