package com.codezlab.groove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicScreen extends AppCompatActivity {
    public int currentSong;
    private ImageView playIcon,previousBTN,nextBTN;
    private TextView title,artist,currentDuration,finalDuration;
    private SeekBar songProgress;
    public MediaPlayer player;
    public Drawable drawablePlayIcon;
    public Drawable drawablePauseIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_screen);
        playIcon = findViewById(R.id.playMusicIcon);
        previousBTN = findViewById(R.id.previous_song);
        nextBTN = findViewById(R.id.next_song);
        title = findViewById(R.id.title_view);
        artist = findViewById(R.id.artist_view);
        currentDuration = findViewById(R.id.current_duration);
        finalDuration = findViewById(R.id.final_duration);
        songProgress = findViewById(R.id.song_progress);
        currentSong = getIntent().getIntExtra("CURRENT_POSITION",0);
        setup();
        drawablePlayIcon = getResources().getDrawable(R.drawable.play_btn);
        drawablePauseIcon = getResources().getDrawable(R.drawable.pause_btn);

        player = new MediaPlayer();
        playSong(HomeFragment.musicFiles.get(currentSong).getPath());
        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.isPlaying()){
                    player.pause();
                    playIcon.setImageDrawable(drawablePlayIcon);
                }else {
                    player.start();
                    playIcon.setImageDrawable(drawablePauseIcon);
                }
            }
        });
        previousBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevSong();
            }
        });
        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSong();
            }
        });
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (player.isPlaying()){
                    songProgress.setMax(player.getDuration());
                    songProgress.setProgress(player.getCurrentPosition());
                    currentDuration.setText(durationConverter((long) player.getCurrentPosition()));
                }
                return true;
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                handler.postDelayed(this,1000);
            }
        };
        handler.postDelayed(runnable,1000);
    }
    public void playSong(String path){
        try {
            player.setDataSource(path);
            player.prepare();
            player.start();
            updateSongProgress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void playPause(){
        if (player.isPlaying()){
            player.pause();
        }else {
            player.start();
        }
    }
    public void nextSong(){
        player.pause();
        player.reset();
        currentSong = currentSong+1;
        playSong(HomeFragment.musicFiles.get(currentSong).getPath());
        setup();
    }
    public void prevSong(){
        if (currentSong>=0){
            player.pause();
            player.reset();
            currentSong = currentSong-1;
            playSong(HomeFragment.musicFiles.get(currentSong).getPath());
            setup();
        }
    }
    public void setup(){
        title.setText(HomeFragment.musicFiles.get(currentSong).getTitle());
        artist.setText(HomeFragment.musicFiles.get(currentSong).getArtist());
        long duration = Long.parseLong(HomeFragment.musicFiles.get(currentSong).getDuration());
        finalDuration.setText(durationConverter(duration));
    }
    public String durationConverter(Long duration){
        Long totalSec = duration/1000;
        long min = totalSec/60;
        long sec = totalSec%60;
        String tmp = String.valueOf(min) + ":" + String.valueOf(sec);
        return tmp;
    }
    public void updateSongProgress(){
        if (player.isPlaying()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentDuration.setText(durationConverter((long) player.getCurrentPosition()));
                    songProgress.setProgress(player.getCurrentPosition());
                }
            }, 1000);
        }
    }
}