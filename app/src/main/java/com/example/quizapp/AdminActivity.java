package com.example.quizapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity {
    //khai báo biến giao diện
    Button addQuestion;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //tạo ứng dụng tràn viền
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Khai báo biến giao diện
        addQuestion = findViewById(R.id.addquestion);
        back = findViewById(R.id.back);

        //Viết lắng nghe sự kiện cho nút quản lý câu hỏi
        addQuestion.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), ShowquestionActivity.class);
            startActivity(i);
        });
        // Viết lắng nghe sự kiện cho nút thoát
        back.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
            builder.setMessage("THOÁT TRANG QUẢN LÍ ADMIN");

            // Nút "Thoát" để thoát chương trình và mở LoginActivity
            builder.setPositiveButton("EXIT", (dialog, which) -> {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
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
        });
    }
}