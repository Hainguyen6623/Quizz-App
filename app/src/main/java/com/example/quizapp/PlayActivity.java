package com.example.quizapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PlayActivity extends AppCompatActivity {
    // Biến giao diện
    private TextView txtQuestion_, txtScore_;
    private String stringAnswer;
    private int intScore = 0;
    private String username_, score_;
    private Button btnA_, btnB_, btnC_, btnD_;

    // Quản lý câu hỏi
    private final Set<String> askedQuestionIds = new HashSet<>();
    private long totalQuestions;
    private static final int MAX_RETRY = 3;
    private int retryCount = 0;
    private boolean isMillionaireMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Ẩn thanh trạng thái
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Ánh xạ component
        btnA_ = findViewById(R.id.btnA_);
        btnB_ = findViewById(R.id.btnB_);
        btnC_ = findViewById(R.id.btnC_);
        btnD_ = findViewById(R.id.btnD_);
        txtScore_ = findViewById(R.id.txtScore_);
        txtQuestion_ = findViewById(R.id.txtQuestion_);
        txtScore_.setText(String.valueOf(intScore));

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        username_ = intent.getStringExtra("stringUsername");
        score_ = intent.getStringExtra("stringScore");
        isMillionaireMode = intent.hasExtra("gameMode");

        // Kiểm tra cơ sở dữ liệu trống
        totalQuestions = getTotalQuestions();
        if (totalQuestions == 0) {
            Toast.makeText(this, "Không có câu hỏi!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Thiết lập sự kiện và bắt đầu trò chơi
        setAnswerButtons();
        loadRandomQuestion();
    }

    // Thiết lập sự kiện cho nút trả lời
    private void setAnswerButtons() {
        View.OnClickListener listener = view -> {
            Button button = (Button) view;
            String selectedAnswer = button.getText().toString();

            if (selectedAnswer.equals(stringAnswer)) {
                updateScore();
                if (isMillionaireMode && askedQuestionIds.size() == 15) {
                    showWinDialog();
                } else {
                    loadRandomQuestion();
                }
            } else {
                if (isMillionaireMode) {
                    new AlertDialog.Builder(this)
                            .setTitle("Rất tiếc")
                            .setMessage("Bạn đã trả lời sai! Bạn dừng lại ở câu hỏi số " + askedQuestionIds.size())
                            .setPositiveButton("OK", (dialog, which) -> exitToHome())
                            .show();
                } else {
                    saveScore();
                    showGameOverDialog();
                }
            }
        };

        btnA_.setOnClickListener(listener);
        btnB_.setOnClickListener(listener);
        btnC_.setOnClickListener(listener);
        btnD_.setOnClickListener(listener);
    }

    // Cập nhật điểm số
    private void updateScore() {
        intScore++;
        txtScore_.setText(String.valueOf(intScore));
    }

    // Hiển thị câu hỏi (Chế độ thường)
    @SuppressLint("Range")
    private void displayQuestion(String questionId, SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM tblquiz WHERE id = ?", new String[]{questionId});
            if (cursor.getCount() == 0) {
                if (retryCount < MAX_RETRY) {
                    retryCount++;
                    loadRandomQuestion();
                } else {
                    showGameOverDialog();
                }
                return;
            }

            if (cursor.moveToFirst()) {
                // Lấy tất cả đáp án từ database
                String correctAnswer = cursor.getString(cursor.getColumnIndex("answer"));
                List<String> answers = new ArrayList<>();
                answers.add(cursor.getString(cursor.getColumnIndex("a")));
                answers.add(cursor.getString(cursor.getColumnIndex("b")));
                answers.add(cursor.getString(cursor.getColumnIndex("c")));
                answers.add(cursor.getString(cursor.getColumnIndex("d")));

                // Xáo trộn thứ tự các đáp án
                Collections.shuffle(answers);

                // Gán đáp án đúng sau khi xáo trộn
                stringAnswer = correctAnswer;
                int correctIndex = answers.indexOf(correctAnswer);
                if (correctIndex != -1) {
                    stringAnswer = answers.get(correctIndex);
                }

                // Hiển thị câu hỏi và đáp án đã xáo trộn
                txtQuestion_.setText(cursor.getString(cursor.getColumnIndex("question")));
                btnA_.setText(answers.get(0));
                btnB_.setText(answers.get(1));
                btnC_.setText(answers.get(2));
                btnD_.setText(answers.get(3));
            }
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    // Hiển thị câu hỏi triệu phú
    @SuppressLint("Range")
    private void displayMillionaireQuestion(Cursor cursor) {
        // Tương tự phương thức displayQuestion nhưng không xáo trộn đáp án
        stringAnswer = cursor.getString(cursor.getColumnIndex("answer"));
        txtQuestion_.setText(cursor.getString(cursor.getColumnIndex("question")));

        // Hiển thị đáp án đúng ở vị trí ngẫu nhiên
        List<String> answers = new ArrayList<>();
        answers.add(cursor.getString(cursor.getColumnIndex("a")));
        answers.add(cursor.getString(cursor.getColumnIndex("b")));
        answers.add(cursor.getString(cursor.getColumnIndex("c")));
        answers.add(cursor.getString(cursor.getColumnIndex("d")));
        Collections.shuffle(answers);

        btnA_.setText(answers.get(0));
        btnB_.setText(answers.get(1));
        btnC_.setText(answers.get(2));
        btnD_.setText(answers.get(3));

        // Kiểm tra mốc quan trọng
        int currentQuestion = askedQuestionIds.size();
        if (currentQuestion == 5 || currentQuestion == 10) {
            showCheckpointDialog(currentQuestion);
        }
    }

    // Hiển thị thông báo mốc
    private void showCheckpointDialog(int level) {
        new AlertDialog.Builder(this)
                .setTitle("Chúc mừng!")
                .setMessage("Bạn đã vượt qua mốc " + level + " câu hỏi!")
                .setPositiveButton("Tiếp tục", null)
                .show();
    }

    // Tải câu hỏi ngẫu nhiên
    private void loadRandomQuestion() {
        if (isMillionaireMode) {
            loadMillionaireQuestion();
        } else {
            if (askedQuestionIds.size() == totalQuestions) {
                showWinDialog();
                return;
            }

            SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
            try {
                String questionId = generateUniqueQuestionId();
                askedQuestionIds.add(questionId);
                displayQuestion(questionId, db);
            } finally {
                db.close();
            }
        }
    }

    // Phương thức mới cho chế độ triệu phú
    private void loadMillionaireQuestion() {
        if (askedQuestionIds.size() >= 15) {
            showWinDialog();
            return;
        }

        SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
        try {
            // Lấy câu hỏi theo level
            int currentLevel = askedQuestionIds.size() + 1;
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM tblquesTp WHERE level = ? ORDER BY RANDOM() LIMIT 1",
                    new String[]{String.valueOf(currentLevel)}
            );

            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String questionId = cursor.getString(cursor.getColumnIndex("id"));
                askedQuestionIds.add(questionId);
                displayMillionaireQuestion(cursor);
            } else {
                // Xử lý trường hợp không tìm thấy câu hỏi
                cursor.close();
                Toast.makeText(this, "Không tìm thấy câu hỏi cho level " + currentLevel, Toast.LENGTH_SHORT).show();
                showGameOverDialog();
            }
            cursor.close();
        } finally {
            db.close();
        }
    }

    // Tạo ID câu hỏi chưa được hỏi
    private String generateUniqueQuestionId() {
        Random random = new Random();
        String id;
        do {
            id = String.valueOf(random.nextInt((int) totalQuestions) + 1);
        } while (askedQuestionIds.contains(id));
        return id;
    }

    // Lưu điểm
    private void saveScore() {
        try {
            int highScore = 0;
            try {
                highScore = Integer.parseInt(score_);
            } catch (NumberFormatException e) {
                Log.w("PlayActivity", "Không có điểm cao nhất trước đó");
            }

            String newScore = Math.max(intScore, highScore) + "";

            SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
            try {
                db.execSQL("UPDATE tbluser SET score = ? WHERE username = ?", new Object[]{newScore, username_});
            } finally {
                db.close();
            }
        } catch (Exception e) {
            Log.e("PlayActivity", "Lỗi lưu điểm: " + e.getMessage());
            Toast.makeText(this, "Lưu điểm thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    // Hiển thị thông báo thua
    private void showGameOverDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Điểm của bạn: " + intScore)
                .setPositiveButton("Chơi lại", (dialog, which) -> restartGame())
                .setNegativeButton("Thoát", (dialog, which) -> exitToHome())
                .show();
    }

    // Hiển thị thông báo thắng
    private void showWinDialog() {
        if (isMillionaireMode && askedQuestionIds.size() == 15) {
            new AlertDialog.Builder(this)
                    .setTitle("Chiến thắng!")
                    .setMessage("Bạn đã trở thành TRIỆU PHÚ!")
                    .setPositiveButton("OK", (dialog, which) -> exitToHome())
                    .setCancelable(false)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Chiến thắng!")
                    .setMessage("Bạn đã trả lời đúng tất cả câu hỏi!")
                    .setPositiveButton("OK", (dialog, which) -> exitToHome())
                    .setCancelable(false)
                    .show();
        }
    }

    // Khởi động lại trò chơi
    private void restartGame() {
        intScore = 0;
        retryCount = 0;
        askedQuestionIds.clear();
        txtScore_.setText("0");
        loadRandomQuestion();
    }

    // Về màn hình chính
    private void exitToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    // Đếm tổng câu hỏi
    private long getTotalQuestions() {
        SQLiteDatabase db = openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
        try {
            if (isMillionaireMode) {
                return 15; // Fixed 15 questions for millionaire mode
            } else {
                return db.compileStatement("SELECT COUNT(*) FROM tblquiz").simpleQueryForLong();
            }
        } finally {
            db.close();
        }
    }
}