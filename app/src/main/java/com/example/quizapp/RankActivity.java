package com.example.quizapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton back;
    private final ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Ánh xạ view
        recyclerView = findViewById(R.id.listview);
        back = findViewById(R.id.back);

        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RankAdapter(users));

        // Sự kiện nút back
        back.setOnClickListener(view -> finish());

        // Load dữ liệu
        DisplayRank();
    }

    private void DisplayRank() {
        SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM tbluser ORDER BY CAST(score AS INTEGER) DESC", null);

        int id = c.getColumnIndex("id");
        int username = c.getColumnIndex("username");
        int score = c.getColumnIndex("score");

        users.clear();
        if (c.moveToFirst()) {
            do {
                User user = new User();
                user.id = c.getString(id);
                user.username = c.getString(username);
                user.score = c.getString(score);
                users.add(user);
            } while (c.moveToNext());
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        c.close();
        db.close();
    }
}