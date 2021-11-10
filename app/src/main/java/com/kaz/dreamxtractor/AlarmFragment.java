package com.kaz.dreamxtractor;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private NavController navController;
    private ImageView listBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //migracion de variables del main
    Calendar cal_alarm = Calendar.getInstance();
    Calendar cal_now = Calendar.getInstance();
    TimePicker timePicker;
    TextView timetext;
    int mHour=12,mMin=0;
    String timeStr;
    private Button oneTimeBtn;
    private Button repeatBtn;
    private Button cancelBtn;



    public AlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {

        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        listBtn= view.findViewById(R.id.record_list_btn);
        listBtn.setOnClickListener(this);

        //Migracion de main
        timePicker=(TimePicker)view.findViewById(R.id.timePicker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(12);
            timePicker.setMinute(0);
        }

        timetext=view.findViewById(R.id.TimeText);

        oneTimeBtn = view.findViewById(R.id.button2);
        repeatBtn = view.findViewById(R.id.button);
        cancelBtn = view.findViewById(R.id.button4);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                mHour= hourOfDay;
                mMin= minute;
                if(mMin<10)
                    timeStr=mHour+":0"+mMin;
                else
                    timeStr=mHour+":"+mMin;
                timetext.setText(timeStr);

            }
        });

        oneTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTimer();
                ((MainActivity)getActivity()).setOneTime(cal_alarm.getTimeInMillis());
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTimer();
                ((MainActivity)getActivity()).setRepeat(cal_alarm.getTimeInMillis());
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timetext.setText("not set");
                setTimer();
                ((MainActivity)getActivity()).cancel();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

                return inflater.inflate(R.layout.fragment_alarm, container, false);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.record_list_btn:
                navController.navigate(R.id.action_alarmFragment_to_audioListFragment);
                break;
        }
    }


    //Migracion del main

    public void setTimer(){
        Date date = new Date();
        setTime();
        cal_now.setTime(date);
        cal_alarm.setTime(date);

        cal_alarm.set(Calendar.HOUR_OF_DAY,mHour);
        cal_alarm.set(Calendar.MINUTE,mMin);
        cal_alarm.set(Calendar.SECOND,0);

        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE,1);
        }
    }
    public void setTime(){
        if(mMin<10)
            timeStr=mHour+":0"+mMin;
        else
            timeStr=mHour+":"+mMin;
        timetext.setText("set to "+timeStr);
    }




}

