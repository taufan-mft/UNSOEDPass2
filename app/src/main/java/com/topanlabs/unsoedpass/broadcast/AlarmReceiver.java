package com.topanlabs.unsoedpass.broadcast;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.topanlabs.unsoedpass.MainActivity;
import com.topanlabs.unsoedpass.R;
import com.topanlabs.unsoedpass.memoList;
import com.topanlabs.unsoedpass.setReminder;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    //private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
     NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String judul = intent.getStringExtra("Judul");
        String konten = intent.getStringExtra("konten");
        int NOTIFICATION_ID = intent.getIntExtra("notifID", 0);
        int jam = intent.getIntExtra("hour",0);
        int menit = intent.getIntExtra("minute", 0);
        int dayofweek = intent.getIntExtra("dayofweek", 0);
        Boolean repeat = intent.getBooleanExtra("repeat", false);
        String afterAction = intent.getStringExtra("afterAction");
        Intent contentIntent = new Intent(context, MainActivity.class);
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (afterAction.equals("main")) {
             contentIntent = new Intent(context, MainActivity.class);
        } else if (afterAction.equals("memo")){
            contentIntent =  new Intent(context, memoList.class);
        }
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.sudirpong2)
                .setContentTitle(judul)
                .setContentText(konten)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        Log.d("aurel","oke");
        Calendar calNow = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, jam);
        calendar.set(Calendar.MINUTE, menit);
        calendar.set(Calendar.DAY_OF_WEEK, dayofweek);
        calendar.set(Calendar.SECOND, 0);
        if(calendar.before(calNow))
        {
            // If it's in the past increment by one week.
            calendar.add(Calendar.DATE, 7);
        }
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("Judul", judul);
        myIntent.putExtra("konten", konten);
        myIntent.putExtra("notifID",NOTIFICATION_ID);
        myIntent.putExtra("hour", jam);
        myIntent.putExtra("minute", menit);
        myIntent.putExtra("dayofweek", dayofweek);
        myIntent.putExtra("afterAction", afterAction);
        myIntent.putExtra("repeat", repeat);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (repeat) {
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= 23) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
    }
}}
