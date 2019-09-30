package com.topanlabs.unsoedpass;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class matkulViewModel extends AndroidViewModel {
    private matkulRepository mRepository;
    private LiveData<List<matkuldb>> allMatkul;

    public matkulViewModel (Application application) {
        super(application);
        mRepository = new matkulRepository(application);
        allMatkul = mRepository.getAll();
    }

    LiveData<List<matkuldb>> getAll() { return allMatkul; }

    public void insert(matkuldb matkuldb) { mRepository.insert(matkuldb); }
}
