package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class ShowquestionActivity extends AppCompatActivity
        implements
        AddQuestionDialogFragment.OnQuestionAddedListener,
        UpdateQuestionDialogFragment.OnQuestionUpdatedListener {

    private ListView listView;
    private ImageButton back;
    private FloatingActionButton fabAddQuestion;
    private ArrayList<Question> questions = new ArrayList<>();
    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showquestion);

        // Ẩn thanh trạng thái
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        // Ánh xạ view
        listView = findViewById(R.id.listview);
        back = findViewById(R.id.back);
        fabAddQuestion = findViewById(R.id.fab_add_question);

        // Khởi tạo adapter
        adapter = new QuestionAdapter(this, questions);
        listView.setAdapter(adapter);

        // Xử lý nút back
        back.setOnClickListener(v -> showExitDialog());

        // Xử lý FAB - Mở dialog thêm câu hỏi
        fabAddQuestion.setOnClickListener(v -> {
            AddQuestionDialogFragment dialog = new AddQuestionDialogFragment();
            dialog.show(getSupportFragmentManager(), "AddQuestionDialog");
        });

        // Tải danh sách câu hỏi ban đầu
        displayQuestions();
    }

    // Callback khi thêm câu hỏi thành công
    @Override
    public void onQuestionAdded() {
        displayQuestions(); // Cập nhật danh sách
    }


    @Override
    public void onQuestionUpdated() {
        displayQuestions(); // Cập nhật danh sách
    }

    // Hiển thị dialog xác nhận thoát
    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát trang")
                .setMessage("Bạn có chắc muốn thoát khỏi danh sách câu hỏi?")
                .setPositiveButton("Thoát", (dialog, which) -> finish())
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Xử lý xóa câu hỏi
    public void deleteQuestion(String id) {
        try {
            SQLiteDatabase db = openOrCreateDatabase("QuizDB", MODE_PRIVATE, null);
            db.delete("tblquiz", "id=?", new String[]{id});
            Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
            displayQuestions(); // Làm mới danh sách
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Tải và hiển thị danh sách câu hỏi
    @SuppressLint("Range")
    private void displayQuestions() {
        SQLiteDatabase db = openOrCreateDatabase("QuizDB", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM tblquiz", null);

        questions.clear();
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.id = cursor.getString(cursor.getColumnIndex("id"));
                question.question = cursor.getString(cursor.getColumnIndex("question"));
                question.answer = cursor.getString(cursor.getColumnIndex("answer"));
                question.a = cursor.getString(cursor.getColumnIndex("a"));
                question.b = cursor.getString(cursor.getColumnIndex("b"));
                question.c = cursor.getString(cursor.getColumnIndex("c"));
                question.d = cursor.getString(cursor.getColumnIndex("d"));
                questions.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged(); // Cập nhật UI
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayQuestions(); // Cập nhật khi quay lại từ màn hình khác
    }
}