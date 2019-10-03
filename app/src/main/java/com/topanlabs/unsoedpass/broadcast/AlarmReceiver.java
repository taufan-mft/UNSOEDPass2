package com.topanlabs.unsoedpass.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.topanlabs.unsoedpass.MainActivity;
import com.topanlabs.unsoedpass.R;

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

        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.sudirpong)
                .setContentTitle(judul)
                .setContentText(konten)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        Log.d("aurel","oke");

    }
}
