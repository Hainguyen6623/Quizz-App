package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ImageButton rank, imgUser;
    private Button play, play2;
    private TextView txtUsername, txtScore;
    private ArrayList<User> users = new ArrayList<>();
    private String score_;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Thiết lập tràn viền
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Ánh xạ các thành phần
        txtScore = findViewById(R.id.score);
        txtUsername = findViewById(R.id.username);
        play = findViewById(R.id.play);
        play2 = findViewById(R.id.play2);
        rank = findViewById(R.id.rank);
        imgUser = findViewById(R.id.imgUser);


        // Xử lý sự kiện icon người dùng
        imgUser.setOnClickListener(v -> {
            Intent userIntent = new Intent(HomeActivity.this, UserActivity.class);
            userIntent.putExtra("USERNAME", txtUsername.getText().toString());
            startActivity(userIntent);
        });

        // Lấy tên người dùng từ Intent
        Intent intent = getIntent();
        String username_ = intent.getStringExtra("stringUsername");

        // Xử lý nút Play (Chế độ thường)
        play.setOnClickListener(v -> {
            Intent playIntent = new Intent(HomeActivity.this, PlayActivity.class);
            playIntent.putExtra("stringUsername", username_);
            playIntent.putExtra("stringScore", score_);
            startActivity(playIntent);
            Toast.makeText(this, "Bắt đầu trò chơi thường!", Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút Play2 (Chế độ triệu phú)
        play2.setOnClickListener(v -> {
            Intent playIntent = new Intent(HomeActivity.this, PlayActivity.class);
            playIntent.putExtra("stringUsername", username_);
            playIntent.putExtra("gameMode", "millionaire");
            startActivity(playIntent);
            Toast.makeText(this, "Bắt đầu chế độ triệu phú!", Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút Xếp hạng
        rank.setOnClickListener(v -> {
            Intent rankIntent = new Intent(this, RankActivity.class);
            startActivity(rankIntent);
        });

        // Hiển thị thông tin người dùng
        displayUserInfo(username_);
    }

       // Hiển thị thông tin người dùng
    @SuppressLint("Range")
    private void displayUserInfo(String username) {
        SQLiteDatabase db = openOrCreateDatabase("QuizDB", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM tbluser WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            User user = new User();
            user.username = cursor.getString(cursor.getColumnIndex("username"));
            user.score = cursor.getString(cursor.getColumnIndex("score"));

            txtUsername.setText(user.username);
            txtScore.setText(user.score);
            score_ = user.score;
        }
        cursor.close();
        db.close();
    }
}