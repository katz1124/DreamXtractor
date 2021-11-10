package com.kaz.dreamxtractor;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.sql.Time;


public class AudioListFragment extends Fragment implements AudioListAdapter.onItemListClick{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AudioListAdapter audioListAdapter;
    private MediaPlayer mediaPlayer=null;
    private boolean isPlaying=false;
    private NavController navController;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BottomSheetBehavior bottomSheetBehavior;
    private ConstraintLayout playerSheet;
    private RecyclerView audioList;

    private File[] allFiles;
    private File fileToPlay;

    //UI ELE
    private ImageButton playBtn;
    private TextView playerHeader;
    private TextView playerFilename;
    private SeekBar playerSeekBar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;

    private ImageButton nextBtn;
    private ImageButton rebBtn;

    public AudioListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerSheet = view.findViewById(R.id.player_sheet);
        bottomSheetBehavior=BottomSheetBehavior.from(playerSheet);
        audioList = view.findViewById(R.id.audio_list_view);
        playBtn=view.findViewById(R.id.player_play_btn);
        playerHeader=view.findViewById(R.id.player_header_title);
        playerFilename=view.findViewById(R.id.player_file_name);
        nextBtn=view.findViewById(R.id.next_button);
        rebBtn=view.findViewById(R.id.reb_button);
        playerSeekBar = view.findViewById(R.id.player_seekbar);
        navController= Navigation.findNavController(view);

        String path=getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory= new File(path);
        allFiles=directory.listFiles();

        audioListAdapter=new AudioListAdapter(allFiles,this);

        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.setAdapter(audioListAdapter);



        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        rebBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    pauseAudio();
                    mediaPlayer.seekTo(0);
                    resumeAudio();
                }else{
                    if(fileToPlay!=null){
                        mediaPlayer.seekTo(0);

                    }

                }
                playerSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        });

       nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int actual=mediaPlayer.getCurrentPosition();
                int duration=mediaPlayer.getDuration();
                if(duration-actual>6000){
                    if(isPlaying){
                        pauseAudio();
                        mediaPlayer.seekTo(actual+5000);
                        resumeAudio();

                    }else {
                     if (fileToPlay != null) {
                          mediaPlayer.seekTo(actual+5000);
                     }
                 }
                }
                playerSeekBar.setProgress(mediaPlayer.getCurrentPosition()+5000);


            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    pauseAudio();
                }else{
                    if(fileToPlay!=null){
                        resumeAudio();
                    }

                }
            }
        });

        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(fileToPlay!=null){
                    int progress= seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    resumeAudio();
                }

            }
        });

    }

    @Override
    public void onClickListener(File file, int position) {
        Log.d("Play Log", "file playing"+file.getName());

        fileToPlay=file;

        if(isPlaying){
            stopAudio();
            playAudio(fileToPlay);

        }
        else{

            playAudio(fileToPlay);

        }



    }

    private void pauseAudio(){
        mediaPlayer.pause();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn,null));
        isPlaying=false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }
    private void resumeAudio(){
        mediaPlayer.start();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn,null));
        isPlaying=true;
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar,0);
    }

    private void stopAudio() {
        //stop audio
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn,null));
        playerHeader.setText("Stopped");
        isPlaying=false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void playAudio(File fileToPlay) {

        mediaPlayer=new MediaPlayer();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn,null));
        playerFilename.setText(fileToPlay.getName());
        playerHeader.setText("Playing");
        //play the audio
        isPlaying=true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                pauseAudio();
                playerHeader.setText("Finished");
                mediaPlayer.seekTo(0);
                playerSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        });
        playerSeekBar.setMax(mediaPlayer.getDuration());

        seekbarHandler=new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar,0);
    }

    private void updateRunnable() {
        updateSeekbar= new Runnable() {
            @Override
            public void run() {
                playerSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this,500);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isPlaying){
            stopAudio();
        }

    }
}

