package com.koohpar.eram.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.koohpar.eram.R;
import com.koohpar.eram.activities.ListMessageActivity;
import com.koohpar.eram.activities.RateActivity;
import com.koohpar.eram.tools.AppConstants;

import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Behnaz on 06/10/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static int badgeCount=0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("type"), remoteMessage.getData().get("ldate"));
    }

    private void showNotification(String message, String type, String ldate) {
        Context context = getBaseContext();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock w1 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            w1.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire(10000);
        }

        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);
//        int notificationId = Integer.valueOf(1);
        Intent notifyIntent;
        if (type.equalsIgnoreCase("3"))
            notifyIntent = new Intent(context, ListMessageActivity.class);
        else
            notifyIntent = new Intent(context, RateActivity.class);
        notifyIntent.putExtra(AppConstants.KEY_NOTIFICATION_TYPE, type);
        notifyIntent.putExtra(AppConstants.KEY_NOTIFICATION_MESSAGE, message);
        notifyIntent.putExtra(AppConstants.KEY_NOTIFICATION_DATE, ldate);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notifyIntent.setAction(Intent.ACTION_MAIN);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        notificationId,
                        notifyIntent,
                        PendingIntent.FLAG_ONE_SHOT
                );
        long pattern[] = {0, 100, 200, 300};
        Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notifSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.noti);//Here is FILE_NAME is the name of file that you want to play

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)

                .setContentIntent(notifyPendingIntent)

                .setSmallIcon(getNotificationIcon()).setColor(Color.parseColor("#c42626"))

                .setContentTitle(context.getString(R.string.app_name))

                .setVibrate(pattern)

                .setSound(notifSound)
                .setAutoCancel(true)

                .setContentText(message);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (type.equalsIgnoreCase("3")) {
            badgeCount ++;
            ShortcutBadger.applyCount(context, badgeCount); //for 1.1.4+
            mNotificationManager.notify(notificationId, mBuilder.build());
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            RateActivity.newString = ldate;
            Intent intent;
            intent = new Intent(context, RateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.drawable.logo : R.drawable.logo;
    }
}
