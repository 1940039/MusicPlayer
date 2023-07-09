package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {
    private EditText userEmail, userPass, confirmPassword;
    private SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        SharedPreferences sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userEmail = findViewById(R.id.user_email);
        userPass = findViewById(R.id.user_pass);
        confirmPassword = findViewById(R.id.confirm_password);
        Button create_account_btn = findViewById(R.id.create_account_btn);

        create_account_btn.setOnClickListener(view -> accountUser());
    }

    private void accountUser() {
        String email = userEmail.getText().toString();
        String password = userPass.getText().toString();
        String confirm = confirmPassword.getText().toString();

        // verifica se os campos de email e senha não estão vazios
        if (email.isEmpty()) {
            userEmail.setError("Insira o email");
            userEmail.requestFocus();
        } else if (password.isEmpty()) {
            userPass.setError("Insira a senha");
            userPass.requestFocus();
        } else if (confirm.isEmpty()) {
            confirmPassword.setError("Insira a senha novamente para confirmar");
            confirmPassword.requestFocus();
        } else if (!password.equals(confirm)) {
            //verifica se a senha foi digitada corretamente
            confirmPassword.setError("A senhas digitadas não correspondem");
            confirmPassword.requestFocus();
        } else {
            // armazena as informações do usuário no SharedPreferences
            editor.putString("email", email);
            editor.putString("password", password);
            editor.commit();

            Toast.makeText(this, "Conta cadastrada com sucesso!", Toast.LENGTH_SHORT).show();

            // retorna para a tela de login após o cadastro
            finish();
        }
    }
}