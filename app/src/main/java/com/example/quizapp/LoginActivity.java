package com.example.quizapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {


    //    //Khai báo biến giao diện
    EditText editUsername, editPassword;
    TextView txtSignup;
    Button btnLogin;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //tạo ứng dụng tràn viền
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Ánh xạ biến giao diện
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        txtSignup = findViewById(R.id.txtSignup);
        btnLogin = findViewById(R.id.btnLogin);


        //Viết Lắng nghe sự kiện cho nút đăng nhập
        btnLogin.setOnClickListener(view -> {
            // Chuyển đổi dữ liệu nhập vào sang kiểu chuỗi
            String editUsername_ = editUsername.getText().toString();
            String editPassword_ = editPassword.getText().toString();

            // Kiểm tra chuỗi nhập vào có trống không
            if (TextUtils.isEmpty(editUsername_) || TextUtils.isEmpty(editPassword_)) {
                Toast.makeText(LoginActivity.this, "Vui lòng không để trống dữ liệu", Toast.LENGTH_SHORT).show();
            } else if (editUsername_.equals("admin") && editPassword_.equals("admin")) {
                // Nếu đúng tài khoản mật khẩu admin thì mở trang quản lý của admin
                 Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                 startActivity(i);
                 finish();
            } else {
                signinUser(editUsername_, editPassword_);
                finish();
            }
        });



        // viết lắng nghe sự kiện cho nút đăng kí
        txtSignup.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });
    }

    //Viết hàm xử lý sự kiện cho nút đăng nhập
    private void signinUser(String editUsername, String editPassword) {


           SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
           final Cursor c = db.rawQuery("select * from tbluser where username = ? and password = ?", new String[]{editUsername, editPassword});

            if (c.getCount() > 0) {
                //Nếu c truy xuất ra được dữ liệu trùng khớp thì Đăng nhập vào HomeActivity
                 Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                 i.putExtra("stringUsername", editUsername);
                 startActivity(i);

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("ĐĂNG NHẬP KHÔNG THÀNH CÔNG, BẠN CÓ MUỐN ĐĂNG KÝ?");

                // Nút "Thoát" để thoát chương trình và mở LoginActivity
                builder.setPositiveButton("SIGN", (dialog, which) -> {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });

                // Nút "Ở lại" để thoát dialog và tiếp tục ở lại màn hình hiện tại
                builder.setNegativeButton("CANCLE", (dialog, which) -> {
                    dialog.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

    }
}