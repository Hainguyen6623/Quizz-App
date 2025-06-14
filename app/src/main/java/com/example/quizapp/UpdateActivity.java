package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateActivity extends AppCompatActivity {
    //Khai báo biến giao diện
    EditText question, answer, a, b, c, d, id;
    Button submit, clear;
    ImageButton back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //tạo ứng dụng tràn viền
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Ánh xạ biến giao diện
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        a = findViewById(R.id.ca);
        b = findViewById(R.id.cb);
        c = findViewById(R.id.cc);
        d = findViewById(R.id.cd);
        id = findViewById(R.id.id);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        clear = findViewById(R.id.clear);

        //Nhận dữ liệu từ Showquestion Activity
        Intent i = getIntent();

        //Gán kiểu dữ liệu cho các biến lấy từ SA
        String _id = i.getStringExtra("id");
        String _answer = i.getStringExtra("answer");
        String _question = i.getStringExtra("question");
        String _a = i.getStringExtra("a");
        String _b = i.getStringExtra("b");
        String _c = i.getStringExtra("c");
        String _d = i.getStringExtra("d");


        //Gán giá trị vừa lấy cho các biến giao diện
        question.setText(_question);
        answer.setText(_answer);
        id.setText(_id);
        a.setText(_a);
        b.setText(_b);
        c.setText(_c);
        d.setText(_d);

        //Viết lắng nghe sự kiện cho nút clear
        clear.setOnClickListener(view -> {
            question.setText("");
            answer.setText("");
            a.setText("");
            b.setText("");
            c.setText("");
            d.setText("");
        });
        //Viết lắng nghe sự kiện click cho nút submit
        submit.setOnClickListener(view -> {
            //Lấy dữ liệu từ các biến giao diện
            String question_ = question.getText().toString().trim();
            String answer_ = answer.getText().toString().trim();
            String id_ = id.getText().toString().trim();
            String a_ = a.getText().toString().trim();
            String b_ = b.getText().toString().trim();
            String c_ = c.getText().toString().trim();
            String d_ = d.getText().toString().trim();


            //Bẫy dữ liệu nhập rỗng vào của người dùng
            if(question_.equals("") || answer_.equals("") || id_.equals("") || a_.equals("") || b_.equals("") || c_.equals("") || d_.equals("")){
                Toast.makeText(UpdateActivity.this, "Vui lòng không để rỗng dữ liệu", Toast.LENGTH_SHORT).show();
            } else {
                Update(id_, question_, answer_, a_, b_, c_, d_);
            }
        });

        back.setOnClickListener(view -> {
            Intent i1 = new Intent(getApplicationContext(), ShowquestionActivity.class);
            startActivity(i1);
        });
    }
    public void Update(String id, String question, String answer, String a, String b, String c, String d){
        try {
            SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);

            String sql = "update tblquiz set question = ?, answer = ?, a = ?, b = ?, c = ?, d = ? where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            //Gắn giá trị vào các vị trí trên
            statement.bindString(1,question);
            statement.bindString(2,answer);
            statement.bindString(3,a);
            statement.bindString(4,b);
            statement.bindString(5,c);
            statement.bindString(6,d);
            statement.bindString(7,id);
            statement.execute();
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}