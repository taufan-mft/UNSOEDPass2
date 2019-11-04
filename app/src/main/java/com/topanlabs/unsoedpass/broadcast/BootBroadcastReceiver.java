package com.topanlabs.unsoedpass.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.topanlabs.unsoedpass.matkulRepository;
import com.topanlabs.unsoedpass.matkuldb;
import com.topanlabs.unsoedpass.setReminder;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class BootBroadcastReceiver extends BroadcastReceiver {
    matkulRepository matkulRepository;
    List<matkuldb> dataList;
    int matkulcount;
    @Override
    public void onReceive(Context pContext, Intent intent) {
        // Do your work related to alarm manager
        Log.d("bootsuda","nih disini");

        matkulRepository = new matkulRepository(pContext.getApplicationContext());
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dataList = matkulRepository.getAllMat();
                matkulcount = matkulRepository.getCount2();
                for (int i = 0; i <= matkulcount - 1; i++) {
                    AlarmManager manager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
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
                    jam = jam - 1;
                    String jam2 = dataList.get(i).getJam().substring(0, 2);
                    String menit2 = dataList.get(i).getJam().substring(2, 4);
                    Integer menit = Integer.parseInt(dataList.get(i).getJam().substring(2, 4));
                    String ruangan = dataList.get(i).getRuangan();
                    String brigita = "Kuliah dimulai jam " + jam2 + "." + menit2 + ", di " + ruangan;
                    Calendar calNow = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, jam);
                    calendar.set(Calendar.MINUTE, menit);
                    calendar.set(Calendar.DAY_OF_WEEK, winul);
                    calendar.set(Calendar.SECOND, 0);
                    if (calendar.before(calNow)) {
                        // If it's in the past increment by one week.
                        calendar.add(Calendar.DATE, 7);
                    }

                    Intent myIntent = new Intent(pContext, AlarmReceiver.class);
                    myIntent.putExtra("Judul", namatkul);
                    myIntent.putExtra("konten", brigita);
                    myIntent.putExtra("notifID", i);
                    myIntent.putExtra("hour", jam);
                    myIntent.putExtra("minute", menit);
                    myIntent.putExtra("dayofweek", winul);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, i, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    Log.d("aurel", "kuliah: " + namatkul + "Jam: " + jam + ":" + menit + "hari" + winul + " " + hari);
                }
            }
        });

    }
}

