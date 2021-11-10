package com.kaz.dreamxtractor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import android.app.AlarmManager;

import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private AlarmManagerBroadcastReceiver alarm;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        alarm = new AlarmManagerBroadcastReceiver();





    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setOneTime(long timeInMils){

        Context context = getApplicationContext();
        if(alarm != null)
            alarm.setOnetimeTimer(context,timeInMils);
    }
    public void setRepeat(long timeInMils){

        Context context = getApplicationContext();
        if(alarm != null)
            alarm.SetAlarm(context,timeInMils);
    }
    public void cancel(){
        Log.i("testeo", "realizo el metodo :)");
        Context context = getApplicationContext();
        if(alarm != null)
            alarm.CancelAlarm(context);
    }





    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        Uri soundUri = Uri.parse(
                "android.resource://" +
                        getApplicationContext().getPackageName() +
                        "/" +
                        R.raw.blu);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "dreamxtractor";
            String description = "alarm to record dreams";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("dreamX03", name, importance);
            channel.setDescription(description);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            channel.setSound(soundUri, audioAttributes);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);
        }
    }
}