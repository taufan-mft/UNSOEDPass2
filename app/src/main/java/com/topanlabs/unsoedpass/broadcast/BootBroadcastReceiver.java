package com.topanlabs.unsoedpass.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.topanlabs.unsoedpass.kelaspenggantidb.kelasRepository;
import com.topanlabs.unsoedpass.kelaspenggantidb.kelaspengganti;
import com.topanlabs.unsoedpass.matkulRepository;
import com.topanlabs.unsoedpass.matkuldb;
import com.topanlabs.unsoedpass.memo.memoent;
import com.topanlabs.unsoedpass.memo.memorepo;
import com.topanlabs.unsoedpass.setReminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class BootBroadcastReceiver extends BroadcastReceiver {
    matkulRepository matkulRepository;
    kelasRepository kelasRepository;
    memorepo memoRepository;
    List<matkuldb> dataList;
    List<kelaspengganti> kelaspenggantis;
    List<memoent> memos;
    int matkulcount;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context pContext, Intent intent) {
        // Do your work related to alarm manager
        Log.d("bootsuda", "nih disini");
        mSettings = pContext.getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        boolean reminderon = mSettings.getBoolean("reminderon", false);
        matkulRepository = new matkulRepository(pContext.getApplicationContext());
        kelasRepository = new kelasRepository(pContext.getApplicationContext());
        memoRepository = new memorepo(pContext.getApplicationContext());
        if (reminderon) {
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
                        Log.d("bootsuda", "cihuyy");
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
                        myIntent.putExtra("repeat", true);
                        myIntent.putExtra("afterAction", "main");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, i, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        if (Build.VERSION.SDK_INT >= 23) {
                            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        } else {
                            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                        Log.d("aurel", "kuliah: " + namatkul + "Jam: " + jam + ":" + menit + "hari" + winul + " " + hari);
                    }
                }
            });
        }
        //MARK: Kelas pengganti
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                kelaspenggantis = kelasRepository.getKelas();
                Log.d("ahay","size: " + kelaspenggantis.size());
                memos = memoRepository.getKelas();
                int requestCode = 60;
if (kelaspenggantis.size() != 0) {

                for (int i = 0; i < kelaspenggantis.size(); i++) {
                    Log.d("ahay", "ini i nya " + i);
                    AlarmManager manager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
                    String namatkul = kelaspenggantis.get(i).getNamakul() + " " + new String(Character.toChars(0x1F500));
                    Log.d("zhafarin", "ini namatkul nya " + namatkul);
                    String jam = kelaspenggantis.get(i).getJam().substring(0, 2);
                    String menit = kelaspenggantis.get(i).getJam().substring(3, 5);
                    String ruangan = kelaspenggantis.get(i).getRuangan();
                    String tanggal = kelaspenggantis.get(i).getTanggal();
                    String brigita = "Kuliah dimulai jam " + jam + "." + menit + ", di " + ruangan;
                    SimpleDateFormat formatterTanggal = new SimpleDateFormat("dd-MM-yyyy");
                    Date waktu1 = new Date();
                    try {
                        waktu1 = formatterTanggal.parse(tanggal);
                    } catch (Exception e) {
                        //
                    }
                    Calendar c = Calendar.getInstance();
                    c.setTime(waktu1);
                    int day = c.get(Calendar.DAY_OF_WEEK);
                    Calendar calNow = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(jam) - 1);
                    calendar.set(Calendar.MINUTE, Integer.parseInt(menit));
                    calendar.set(Calendar.DAY_OF_WEEK, day);
                    calendar.set(Calendar.SECOND, 0);
                    if (calendar.before(calNow)) {
                        // If it's in the past increment by one week.
                        continue;
                    }
                    Intent myIntent = new Intent(pContext, AlarmReceiver.class);
                    myIntent.putExtra("Judul", namatkul);
                    myIntent.putExtra("konten", brigita);
                    myIntent.putExtra("notifID", requestCode);
                    myIntent.putExtra("hour", jam);
                    myIntent.putExtra("minute", menit);
                    myIntent.putExtra("dayofweek", day);
                    myIntent.putExtra("repeat", false);
                    myIntent.putExtra("afterAction", "main");


                    PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    requestCode++;

                    if (Build.VERSION.SDK_INT >= 23) {
                        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else {
                        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }
            }
 requestCode = 150;

                    for (int i = 0; i < memos.size(); i++) {
                        Log.d("zhafarin", "ini i nya " + i);
                        AlarmManager manager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
                        String jenis= memos.get(i).getJenis()+", ";
                        String namatkul = jenis+memos.get(i).getNamakul();
                        Log.d("zhafarin", "ini namatkul nya " + namatkul);
                        String jam = memos.get(i).getJam().substring(0, 2);
                        String menit = memos.get(i).getJam().substring(3, 5);
                        String ruangan = memos.get(i).getRuangan();
                        String tanggal =memos.get(i).getTanggal();
                        String brigita = "Besok jam " + jam + "." + menit + ", di " + ruangan;
                        SimpleDateFormat formatterTanggal = new SimpleDateFormat("dd-MM-yyyy");
                        Date waktu1 = new Date();
                        try {
                            waktu1 = formatterTanggal.parse(tanggal);
                        } catch (Exception e) {
                            //
                        }
                        Calendar c = Calendar.getInstance();
                        c.setTime(waktu1);
                        int day = c.get(Calendar.DAY_OF_WEEK);
                        Calendar calNow = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(jam));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(menit));
                        calendar.set(Calendar.DAY_OF_WEEK, day-1);
                        calendar.set(Calendar.SECOND, 0);
                        if (calendar.before(calNow)) {
                            // If it's in the past increment by one week.
                            continue;
                        }
                        Intent myIntent = new Intent(pContext, AlarmReceiver.class);
                        myIntent.putExtra("Judul", namatkul);
                        myIntent.putExtra("konten", brigita);
                        myIntent.putExtra("notifID", requestCode);
                        myIntent.putExtra("hour", jam);
                        myIntent.putExtra("minute", menit);
                        myIntent.putExtra("dayofweek", day);
                        myIntent.putExtra("repeat", false);
                        myIntent.putExtra("afterAction", "memo");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        requestCode++;

                        if (Build.VERSION.SDK_INT >= 23) {
                            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        } else {
                            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                        //Log.d("aurel", "kuliah: " + namatkul + "Jam: " + jam + ":" + menit + "hari" + winul + " " + hari);

                    }
            }
        });

    }
}