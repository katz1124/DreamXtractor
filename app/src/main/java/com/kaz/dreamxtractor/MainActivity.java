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

import android.view.View;
import android.widget.Toast;


import android.app.AlarmManager;

import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private AlarmManagerBroadcastReceiver alarm;
    Calendar cal_alarm = Calendar.getInstance();
    Calendar cal_now = Calendar.getInstance();
    TimePicker timePicker;
    TextView timetext;
    int mHour=12,mMin=0;
    String timeStr;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        alarm = new AlarmManagerBroadcastReceiver();
        timePicker=(TimePicker)findViewById(R.id.timePicker);
        timePicker.setHour(12);
        timePicker.setMinute(0);
        timetext=findViewById(R.id.TimeText);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                mHour= hourOfDay;
                mMin= minute;
                timeStr=mHour+":"+mMin;
                timetext.setText(timeStr);

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void startRepeatingTimer(View view) {
        Context context = this.getApplicationContext();
        setTimer();
        if(alarm != null){
            alarm.SetAlarm(context,cal_alarm.getTimeInMillis());
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelRepeatingTimer(View view){
        Context context = this.getApplicationContext();

        if(alarm != null){
            alarm.CancelAlarm(context);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }

    }

    public void onetimeTimer(View view){
        Context context = this.getApplicationContext();
        setTimer();
        if(alarm != null){
            alarm.setOnetimeTimer(context,cal_alarm.getTimeInMillis());
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void setTimer(){
        AlarmManager alarmManager= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Date date = new Date();
        timeStr=mHour+":"+mMin;
        timetext.setText("Set to " + timeStr);
        cal_now.setTime(date);
        cal_alarm.setTime(date);

        cal_alarm.set(Calendar.HOUR_OF_DAY,mHour);
        cal_alarm.set(Calendar.MINUTE,mMin);
        cal_alarm.set(Calendar.SECOND,0);

        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }
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
            NotificationChannel channel = new NotificationChannel("dreamX02", name, importance);
            channel.setDescription(description);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            channel.setSound(soundUri, audioAttributes);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);
        }
    }
}