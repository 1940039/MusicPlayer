package com.example.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView logout, changeScreen;
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        changeScreen = findViewById(R.id.backScreen);
        changeScreen.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Integer> songs = new ArrayList<>();
        songs.add(R.raw.starving);
        songs.add(R.raw.bigbadwolf);
        songs.add(R.raw.sunkissing);

        musicAdapter = new MusicAdapter(songs, this);
        recyclerView.setAdapter(musicAdapter);

        SharedPreferences sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);

        if (sharedPreferences.getString("login", "false").equals("false")) {
            openLogin();
        }
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (view.getId() == R.id.logout) {
            editor.putBoolean("loggedIn", false);
            editor.apply();

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if (view.getId() == R.id.backScreen) {
            startActivity(new Intent(MainActivity.this, PlayerActivity.class));
            finish();
        }
    }

    private void openLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
