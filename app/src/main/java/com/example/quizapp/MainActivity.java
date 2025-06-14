package com.example.quizapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    //Khai báo biến giao diện
    EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    TextView loginPromptTextView;
    Button registerButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tạo ứng dụng tràn viền
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    //Ánh xạ biến giao diện
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        loginPromptTextView = findViewById(R.id.loginPromptTextView);
    registerButton = findViewById(R.id.registerButton);


    //Bắt sự kiện cho nút đăng kí tài khoản
        registerButton.setOnClickListener(view -> {
        //Khai báo và chuyển đổi sang kiểu dữ liệu String cho dữ liệu người dùng nhập vào
        String editUsername_ = usernameEditText.getText().toString();
        String editPasswordSu_ = passwordEditText.getText().toString();
        String editConfirm_passwordSu_ = confirmPasswordEditText.getText().toString();

        // Bẫy lỗi nhập dữ liệu của người dùng
        if (TextUtils.isEmpty(editUsername_) || TextUtils.isEmpty(editPasswordSu_) || TextUtils.isEmpty(editConfirm_passwordSu_)) {
            Toast.makeText(MainActivity.this, "Vui lòng không để trống dữ liệu!!!", Toast.LENGTH_SHORT).show();
        } else if (!editPasswordSu_.equals(editConfirm_passwordSu_)) {
            // Kiểm tra mật khẩu và nhập lại mật khẩu có khớp nhau không
            // Nếu không khớp thì thông báo lỗi
            Toast.makeText(MainActivity.this, "Vui lòng nhập khớp mật khẩu và nhập lại mật khẩu!!!", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu đúng điều kiện thì thực hiện hàm đăng kí tài khoản
                signupUser(editUsername_, editPasswordSu_);
        }
    });
        loginPromptTextView.setOnClickListener(view -> {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    });
    }
    private void signupUser(String editUsername, String editPassword){
        try {
            String editUsername_ = editUsername;
            String editPassword_ = editPassword;

            SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS tbluser (id INTEGER PRIMARY KEY, username VARCHAR, password VARCHAR, score VARCHAR)");

            String sql = "insert into tbluser(username,password,score)values(?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, editUsername_);
            statement.bindString(2, editPassword_);
            statement.bindString(3, "0");
            statement.execute();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("ĐĂNG KÝ THÀNH CÔNG, BẠN CÓ MUỐN ĐĂNG NHẬP?");

            // Nút "Thoát" để thoát chương trình và mở LoginActivity
            builder.setPositiveButton("Login", (dialog, which) -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
        } catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("ĐĂNG KÝ KHÔNG THÀNH CÔNG, NHẬP LẠI?");

            // Nút "Thoát" để thoát chương trình và mở LoginActivity
            builder.setPositiveButton("CLEAR", (dialog, which) -> {
                usernameEditText.setText("");
                passwordEditText.setText("");
                confirmPasswordEditText.setText("");
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