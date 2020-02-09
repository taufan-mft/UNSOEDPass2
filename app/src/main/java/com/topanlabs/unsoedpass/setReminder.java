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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.topanlabs.unsoedpass.broadcast.AlarmReceiver;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class setReminder extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MahasiswaAdapter adapter;
    private matkulViewModel matkulViewModel;
    Integer matkulcount;
    private List<matkuldb> dataList;
    matkulDAO matkulDao;
    private matkulRepository matkulRepository;
    Button btnYa, btnTidak;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);
        mSettings = getSharedPreferences("Settings",0);
        editor = mSettings.edit();
        matkulRepository = new matkulRepository(getApplication());
        btnYa = findViewById(R.id.loginbutton);
        btnTidak = findViewById(R.id.copynom);
        txtStatus = findViewById(R.id.txtStatus);
        boolean reminderon = mSettings.getBoolean("reminderon", false);
        if (reminderon) {
            txtStatus.setText("Reminder aktif.");
        } else {
            txtStatus.setText("Reminder tidak aktif.");
        }
        getData();
        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setingWin(false);
                txtStatus.setText("Reminder aktif.");
                Intent i = new Intent (setReminder.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setingWin(true);
                txtStatus.setText("Reminder aktif.");
                Intent i = new Intent (setReminder.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    private void getData() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dataList = matkulRepository.getAllMat();
                matkulcount = matkulRepository.getCount2();
                Log.d("zhafarin", "matkul count:" +matkulcount);

            }
        });

    }
    public void setingWin(boolean cancel) {
        for (int i = 0; i <= matkulcount-1; i++) {
            Log.d("zhafarin","ini i nya "+i);
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            String namatkul = dataList.get(i).getNamakul();
            Log.d("zhafarin", "nama matkul: "+ namatkul);
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
            jam = jam - 1;
            String jam2 = dataList.get(i).getJam().substring(0, 2);
            String menit2 = dataList.get(i).getJam().substring(2, 4);
            Integer menit = Integer.parseInt(dataList.get(i).getJam().substring(2, 4));
            String ruangan = dataList.get(i).getRuangan();
            String brigita = "Kuliah dimulai jam " + jam2 + "." + menit2 + ", di "+ ruangan;
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
            myIntent.putExtra("hour", jam);
            myIntent.putExtra("minute", menit);
            myIntent.putExtra("dayofweek", winul);
            myIntent.putExtra("repeat", true);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.d("bootsuda","dibuatt");
            if (!cancel) {

                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.d("aurel", "kuliah: " + namatkul + "Jam: " + jam + ":" + menit + "hari" + winul + " " + hari);
            } else {
                manager.cancel(pendingIntent);
                Log.d("aurel", "CANCEL" + namatkul + "Jam: " + jam + ":" + menit + "hari" + winul + " " + hari);
            }
        }
        if (!cancel) {
            editor.putBoolean("reminderon", true);
            editor.apply();
            CharSequence text = "Reminder berhasil dibuat.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        } else {
            editor.putBoolean("reminderon", false);
            editor.apply();
            CharSequence text = "Reminder berhasil di-nonaktifkan";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }
}
