package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    ImageView play, previous, next, cover, changeScreen;
    TextView title;
    SeekBar seekbar_time, seekbar_vol;
    static MediaPlayer mediaPlayer;
    private Runnable runnable;
    private AudioManager audioManager;
    static int currentIndex = 0;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (!isTaskRoot()) {
            finish();
            return;
        }

        if (!checkPermission()) {
            requestPermission();
            return;
        }

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        play = findViewById(R.id.pause_play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        title = findViewById(R.id.song_title);
        cover = findViewById(R.id.cover);
        seekbar_time = findViewById(R.id.seekbar);
        seekbar_vol = findViewById(R.id.seekbar_vol);
        changeScreen = findViewById(R.id.changeScreen);

        final ArrayList<Integer> songs = new ArrayList<>();

        songs.add(0, R.raw.starving);
        songs.add(1, R.raw.bigbadwolf);
        songs.add(2, R.raw.sunkissing);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));

        int maxV = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curV = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekbar_vol.setMax(maxV);
        seekbar_vol.setProgress(curV);

        seekbar_vol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar_time.setMax(mediaPlayer.getDuration());
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                }
                songsNames();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    play.setImageResource(R.drawable.pause);
                }

                if (currentIndex < songs.size() - 1) {
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));

                mediaPlayer.start();
                songsNames();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null) {
                    play.setImageResource(R.drawable.pause);
                }

                if (currentIndex > 0) {
                    currentIndex--;
                } else {
                    currentIndex = songs.size() - 1;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mediaPlayer.start();
                songsNames();
            }
        });

        changeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(PlayerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(PlayerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(PlayerActivity.this, "Permissão solicitada, por favor verifique suas configurações!", Toast.LENGTH_SHORT).show();
        } else
            ActivityCompat.requestPermissions(PlayerActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
    }

    private void songsNames() {
        if (currentIndex == 0) {

            title.setText("Starving - Hailee Steinfeld");
            cover.setImageResource(R.drawable.starving);
        }

        if (currentIndex == 1) {
            title.setText("Big Bad Wolf - Fifth Harmony");
            cover.setImageResource(R.drawable.bigbadwolf);
        }

        if (currentIndex == 2) {
            title.setText("SunKissing - Hailee Steinfeld");
            cover.setImageResource(R.drawable.sunkissing);
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekbar_time.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
            }
        });

        seekbar_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekbar_time.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint({"Handler Leak", "HandlerLeak"})
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            seekbar_time.setProgress(msg.what);
        }
    };
}