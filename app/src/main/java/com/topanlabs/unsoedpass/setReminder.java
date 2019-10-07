package com.topanlabs.unsoedpass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.topanlabs.unsoedpass.broadcast.AlarmReceiver;

import java.util.Calendar;
import java.util.List;

public class setReminder extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MahasiswaAdapter adapter;
    private matkulViewModel matkulViewModel;
    Integer matkulcount;
    private List<matkuldb> dataList;
    matkulDAO matkulDao;

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
                dataList = words;
                setingWin();
            }
        });
        matkulViewModel.getCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                matkulcount = integer;

            }
        });


    }

    public void setingWin() {
        for (int i = 0; i <= matkulcount - 1; i++) {
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            String namatkul = dataList.get(i).getNamakul();
            String hari = dataList.get(i).getHari().toLowerCase();
            int winul = Calendar.MONDAY;

            switch (hari) {
                case "senin":
                    winul = Calendar.MONDAY;
                    break;
                case "selasa":
                    winul = Calendar.TUESDAY;
                    break;
                case "rabu":
                    winul = Calendar.WEDNESDAY;
                    break;
                case "kamis":
                    winul = Calendar.THURSDAY;
                    break;
                case "jumat":
                    winul = Calendar.FRIDAY;
                    break;

            }
            Integer jam = Integer.parseInt(dataList.get(i).getJam().substring(0, 2));
            String jam2 = dataList.get(i).getJam().substring(0, 2);
            String menit2 = dataList.get(i).getJam().substring(2, 4);
            Integer menit = Integer.parseInt(dataList.get(i).getJam().substring(2, 4));
            String brigita = "Kuliah dimulai jam: " + jam2 + ":" + menit2;
            Calendar calNow = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, jam);
            calendar.set(Calendar.MINUTE, menit);
            calendar.set(Calendar.DAY_OF_WEEK, winul);
            calendar.set(Calendar.SECOND, 0);
            if(calendar.before(calNow))
            {
                // If it's in the past increment by one week.
                calendar.add(Calendar.DATE, 7);
            }

            Intent myIntent = new Intent(this, AlarmReceiver.class);
            myIntent.putExtra("Judul", namatkul);
            myIntent.putExtra("konten", brigita);
            myIntent.putExtra("notifID", i);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d("aurel", "kuliah: " + namatkul + "Jam: " + jam + ":" + menit + "hari" + winul + " " + hari);
        }
    }
}
