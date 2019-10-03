package com.topanlabs.unsoedpass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class setReminder extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MahasiswaAdapter adapter;
    private matkulViewModel matkulViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new MahasiswaAdapter(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(setReminder.this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        matkulViewModel = ViewModelProviders.of(this).get(matkulViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        matkulViewModel.getAll().observe(this, new Observer<List<matkuldb>>() {
            @Override
            public void onChanged(@Nullable final List<matkuldb> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });
    }
}