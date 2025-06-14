package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class AddquestionActivity extends AppCompatActivity {
    //Khai báo biến giao diện
    EditText question, answer, a, b, c, d;
    Button submit, view, clear;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestion);

        //tạo ứng dụng tràn viền
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Ánh xạ biến giao diện
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        a = findViewById(R.id.choie_a);
        b = findViewById(R.id.choie_b);
        c = findViewById(R.id.choie_c);
        d = findViewById(R.id.choie_d);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        view = findViewById(R.id.view);
        clear = findViewById(R.id.clear);

        //Kiểm tra và thêm câu hỏi mẫu nếu bảng rỗng
        checkAndInsertSampleQuestions();

        //Viết lắng nghe sự kiện cho nút submit
        submit.setOnClickListener(view -> {
            //gọi biến và gắn kiểu dữ liệu chuỗi
            String question_ = question.getText().toString();
            String answer_ = answer.getText().toString();
            String a_ = a.getText().toString();
            String b_ = b.getText().toString();
            String c_ = c.getText().toString();
            String d_ = d.getText().toString();

            //Bẫy kiểu dữ liệu người dùng nhập vào
            if (question_.equals("") || answer_.equals("") || a.equals("") || b.equals("") || c.equals("") || d.equals("")) {
                Toast.makeText(AddquestionActivity.this, "Vui lòng không để trống dữ liệu nhập vào", Toast.LENGTH_SHORT).show();
            } else {
                //Nếu dữ liệu nhập vào không trống thì thêm câu hỏi
                insertData(question_, answer_, a_, b_, c_, d_);
            }
        });

        //Viết lắng nghe sự kiện quay trở lại màn hình admin
        back.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(i);
        });

        //Viết lắng nghe sự kiện cho nút xem danh sách câu hỏi
        view.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), ShowquestionActivity.class);
            startActivity(i);
        });
    }

    // viết phương thức nhập dữ liệu
    private void insertData(String question, String answer, String a, String b, String c, String d) {
        try {
            SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS tblquiz (id INTEGER PRIMARY KEY AUTOINCREMENT, question VARCHAR, answer VARCHAR, a VARCHAR, b VARCHAR, c VARCHAR, d VARCHAR)");

            String sql = "insert into tblquiz(question, answer, a, b, c, d) values(?,?,?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, question);
            statement.bindString(2, answer);
            statement.bindString(3, a);
            statement.bindString(4, b);
            statement.bindString(5, c);
            statement.bindString(6, d);
            statement.execute();
            Toast.makeText(AddquestionActivity.this, "Nhập dữ liệu thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(AddquestionActivity.this, "Nhập dữ liệu không thành công", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức kiểm tra bảng rỗng và thêm 20 câu hỏi mẫu
    private void checkAndInsertSampleQuestions() {
        try {
            SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS tblquiz (id INTEGER PRIMARY KEY AUTOINCREMENT, question VARCHAR, answer VARCHAR, a VARCHAR, b VARCHAR, c VARCHAR, d VARCHAR)");

            // Kiểm tra xem bảng có rỗng không
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tblquiz", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();

            if (count == 0) {
                // Danh sách 20 câu hỏi mẫu
                String[][] sampleQuestions = {
                        {"Thủ đô của Việt Nam là gì?", "Hà Nội", "Hà Nội", "TP. Hồ Chí Minh", "Đà Nẵng", "Cần Thơ"},
                        {"Sông dài nhất Việt Nam là gì?", "Sông Mekong", "Sông Hồng", "Sông Cửu Long", "Sông Mekong", "Sông Đà"},
                        {"Núi cao nhất Việt Nam là gì?", "Fansipan", "Fansipan", "Ngọc Linh", "Bạch Mã", "Tà Chì Nhù"},
                        {"Việt Nam có bao nhiêu tỉnh thành?", "63", "54", "58", "63", "64"},
                        {"Hồ lớn nhất Việt Nam là gì?", "Hồ Ba Bể", "Hồ Hoàn Kiếm", "Hồ Ba Bể", "Hồ Tây", "Hồ Thác Bà"},
                        {"Quốc hoa của Việt Nam là gì?", "Hoa sen", "Hoa mai", "Hoa đào", "Hoa sen", "Hoa phượng"},
                        {"Vịnh Hạ Long thuộc tỉnh nào?", "Quảng Ninh", "Hải Phòng", "Quảng Ninh", "Thanh Hóa", "Nghệ An"},
                        {"Món phở xuất xứ từ đâu?", "Hà Nội", "Huế", "Hà Nội", "Sài Gòn", "Đà Nẵng"},
                        {"Tên gọi khác của TP. Hồ Chí Minh là gì?", "Sài Gòn", "Gia Định", "Sài Gòn", "Thành phố Mới", "Nam Kỳ"},
                        {"Tác giả của bài thơ 'Nam quốc sơn hà' là ai?", "Lý Thường Kiệt", "Trần Hưng Đạo", "Lý Thường Kiệt", "Nguyễn Trãi", "Ngô Quyền"},
                        {"Việt Nam giành độc lập vào năm nào?", "1945", "1945", "1954", "1975", "1930"},
                        {"Trận chiến Điện Biên Phủ diễn ra năm nào?", "1954", "1945", "1954", "1975", "1968"},
                        {"Đền Hùng thuộc tỉnh nào?", "Phú Thọ", "Vĩnh Phúc", "Phú Thọ", "Hà Nam", "Ninh Bình"},
                        {"Chùa Một Cột nằm ở đâu?", "Hà Nội", "Huế", "Hà Nội", "Đà Nẵng", "TP. Hồ Chí Minh"},
                        {"Món bánh chưng thường ăn vào dịp nào?", "Tết", "Tết", "Trung thu", "Giỗ tổ", "Lễ Vu Lan"},
                        {"Nhạc sĩ Văn Cao là tác giả bài hát nào?", "Tiến quân ca", "Tiến quân ca", "Diễu hành", "Việt Nam quê hương tôi", "Hành khúc ngày và đêm"},
                        {"Việt Nam có bao nhiêu dân tộc?", "54", "53", "54", "55", "56"},
                        {"Khu di tích lịch sử nào được gọi là 'Nhà tù Hỏa Lò'?", "Hà Nội", "Hà Nội", "Huế", "Sài Gòn", "Côn Đảo"},
                        {"Tác giả 'Truyện Kiều' là ai?", "Nguyễn Du", "Nguyễn Trãi", "Nguyễn Du", "Hồ Xuân Hương", "Nguyễn Đình Chiểu"},
                        {"Việt Nam có biên giới với bao nhiêu nước?", "3", "2", "3", "4", "5"}
                };

                // Thêm các câu hỏi mẫu vào bảng
                String sql = "INSERT INTO tblquiz (question, answer, a, b, c, d) VALUES (?, ?, ?, ?, ?, ?)";
                SQLiteStatement statement = db.compileStatement(sql);

                for (String[] q : sampleQuestions) {
                    statement.bindString(1, q[0]);
                    statement.bindString(2, q[1]);
                    statement.bindString(3, q[2]);
                    statement.bindString(4, q[3]);
                    statement.bindString(5, q[4]);
                    statement.bindString(6, q[5]);
                    statement.execute();
                }

                Toast.makeText(AddquestionActivity.this, "Đã thêm 20 câu hỏi mẫu thành công", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(AddquestionActivity.this, "Lỗi khi thêm câu hỏi mẫu: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}