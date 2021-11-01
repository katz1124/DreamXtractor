package com.kaz.dreamxtractor;

import android.Manifest;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DreamRecorder extends AppCompatActivity {

    private ImageButton recordBtn;
    private boolean isRecording=false;
    private String recordPermission= Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE=12;
    
    private MediaRecorder mediaRecorder;
    private String recordFile;
    private Chronometer timer;
    private TextView filenameText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dream_recorder);
        recordBtn= findViewById(R.id.record_btn);
        timer= findViewById(R.id.record_timer);
        filenameText= findViewById(R.id.record_filename);


        recordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isRecording){
                    
                    stopRecording();
                    
                    
                    
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped,null));
                    isRecording=false;
                }
                else{
                    if(checkPermissions()){
                        startRecording();
                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording,null));
                        isRecording=true;
                    }
                    
                }
            }
        });


    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        String recordPath=this.getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss", Locale.CANADA);
        Date now=new Date();
        recordFile="DreamRec_"+formatter.format(now)+".3gp";

        filenameText.setText("Recording, File Name : " + recordFile);


        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath+"/"+recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private void stopRecording() {
        filenameText.setText("Recording Stopped, File Saved ...");
        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;


    }

    private boolean checkPermissions() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    public void stop(View view){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);
    }




}
