package com.topanlabs.unsoedpass;

import android.app.Application;
import android.content.Intent;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)

                .init();
    }
    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey, url, judul;

            if (data != null) {
                customKey = data.optString("customkey", null);
                url = data.optString("url", null);
                judul = data.optString("judul", null);
                if (customKey != null) {
                    if (customKey.equals("memo")) {
                        Intent intent2 = new Intent(getApplicationContext(), memoList.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                    } else if (customKey.equals("kelaspeng")){
                        Intent intent2 = new Intent(getApplicationContext(), kelasPengganti.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                    } else {
                            Intent intent = new Intent(getApplicationContext(), beritaView.class);
                            intent.putExtra("url", url);
                            if (judul != null) {
                                intent.putExtra("judul", judul);
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }

            // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            // startActivity(intent);

            // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
            //   if you are calling startActivity above.
     /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
        }
    }

