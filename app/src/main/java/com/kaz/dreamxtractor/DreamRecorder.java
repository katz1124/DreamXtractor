package com.kaz.dreamxtractor;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class DreamRecorder extends AppCompatActivity {

    private ImageButton recordButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dream_recorder);

    }
    /*
    public void stop(View view){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);
    }

     */
}
