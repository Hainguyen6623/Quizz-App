package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class UserActivity extends AppCompatActivity {
    private TextView txtUserName;
    private Button btnLogout, btnDeleteAccount, btnChangePassword, btnViewAchievements; // Sửa từ CardView -> Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Ánh xạ view
        txtUserName = findViewById(R.id.txtUserName);
        btnLogout = findViewById(R.id.btnLogout); // Ánh xạ trực tiếp vào Button
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnViewAchievements = findViewById(R.id.btnViewAchievements);

        // Nhận tên người dùng từ Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        if (username != null) {
            txtUserName.setText(username);
        }

        // Xử lý sự kiện các nút
        btnLogout.setOnClickListener(v -> logoutUser());
        btnDeleteAccount.setOnClickListener(v -> deleteAccount());
        btnChangePassword.setOnClickListener(v -> changePassword());
        btnViewAchievements.setOnClickListener(v -> viewAchievements());
    }

    // Đăng xuất
    private void logoutUser() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    // Xoá tài khoản
    private void deleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Xoá tài khoản")
                .setMessage("Tất cả dữ liệu sẽ bị xoá vĩnh viễn!")
                .setPositiveButton("Xoá", (dialog, which) -> {
                    // TODO: Thêm logic xoá tài khoản từ CSDL
                    Toast.makeText(this, "Đã xoá tài khoản", Toast.LENGTH_SHORT).show();
                    logoutUser();
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    // Đổi mật khẩu
    private void changePassword() {
        // TODO: Mở Activity/Popup đổi mật khẩu
        Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
    }

    // Xem thành tích
    private void viewAchievements() {
        // TODO: Mở Activity hiển thị lịch sử điểm
        Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
    }
}