package com.kaz.dreamxtractor;




import static android.provider.Settings.System.getString;

import android.app.AlarmManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";





    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if(isScreenOn==false)
        {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"app:MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"app:MyCpuLock");
            wl_cpu.acquire(10000);
        }


        Intent fullScreenIntent = new Intent(context, DreamRecorder.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, 0);


        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getPackageName() + "/" + R.raw.blu);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, "dreamX02")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Wake up neo")
                        .setContentText("Tap to start recording!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setFullScreenIntent(fullScreenPendingIntent, true)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{0, 500, 1000})
                ;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setSound(soundUri);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = notificationBuilder.build();
        notification.sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.blu);
        int NOTIFICATION_ID = 1; // Causes to update the same notification over and over again.
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }





        

    }



    //----------------------------------
    public void SetAlarm(Context context,long cTimeMili)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 5 seconds

        am.setRepeating(AlarmManager.RTC_WAKEUP, cTimeMili, 1000 * 3600 * 24 , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


    public void setOnetimeTimer(Context context,long cTimeMili){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, cTimeMili, pi);
        Log.d("BRD", "alarm man");
    }
}