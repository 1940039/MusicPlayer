package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText userEmail, userPass;
    private TextView txtShow;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userEmail = findViewById(R.id.user_email);
        userPass = findViewById(R.id.user_pass);
        txtShow = findViewById(R.id.txt_show);
        Button loginBtn = findViewById(R.id.login_btn);
        TextView create_account = findViewById(R.id.create_account);

        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("login","false").equals("true")) {
            openDash();
        }

        txtShow.setOnClickListener(view -> {
            if (userPass.getInputType() == 144) {
                userPass.setInputType(129);
                txtShow.setText("Esconder");
            } else {
                userPass.setInputType(144);
                txtShow.setText("Mostrar");
            }
            userPass.setSelection(userPass.getText().length());
        });

        loginBtn.setOnClickListener(view -> validateData());

        create_account.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, AccountActivity.class)));
    }

    private void validateData() {
        String email = userEmail.getText().toString();
        String pass = userPass.getText().toString();

        if (email.isEmpty()) {
            userEmail.setError("Insira o email");
            userEmail.requestFocus();
        } else if (pass.isEmpty()) {
            userPass.setError("Insira a senha");
            userPass.requestFocus();
        } else {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");

            if (email.equals(savedEmail) && pass.equals(savedPassword)) {
                openDash();
            } else {
                Toast.makeText(this, "Confira seu email e senha novamente.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openDash() {
        editor.putString("login","yes");
        editor.commit();
        startActivity(new Intent(LoginActivity.this, PlayerActivity.class));
        finish();
    }
}