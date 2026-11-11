
package com.example.quan_ly_kho_hang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quan_ly_kho_hang.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private TextView txtError;
    private Button btnLogin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        txtError = findViewById(R.id.txtError);
        btnLogin = findViewById(R.id.btnLogin);

        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            txtError.setText("Vui lòng nhập đầy đủ thông tin");
            txtError.setVisibility(View.VISIBLE);
            return;
        }

        if (db.checkLogin(username, password)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            txtError.setText("Sai tên đăng nhập hoặc mật khẩu");
            txtError.setVisibility(View.VISIBLE);
        }
    }
}
