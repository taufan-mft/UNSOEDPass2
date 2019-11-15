package com.topanlabs.unsoedpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;


import java.util.List;
import java.util.concurrent.Executors;

public class absenSelector extends AppCompatActivity {
    RecyclerView recyclerView;
    List<matkuldb>  matkul;
    matkulRepository repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_selector);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(absenSelector.this);
        recyclerView.setLayoutManager(layoutManager);
        getSupportActionBar().setTitle("Pilih Matkul");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                repo = new matkulRepository(getApplication());
                matkul = repo.getAllMat();
                absenSelAdapter adapter = new absenSelAdapter(matkul,getApplicationContext());
                recyclerView.setAdapter(adapter);
                //Log.d("raisan", mahasiswaArrayList.toString());
                adapter.notifyDataSetChanged();
            }
        });


    }
}
