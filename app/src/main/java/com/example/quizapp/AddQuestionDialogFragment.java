package com.example.quizapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AddQuestionDialogFragment extends DialogFragment {
    private EditText etQuestion, etAnswer, etA, etB, etC, etD;
    private OnQuestionAddedListener listener;

    // Interface để cập nhật danh sách
    public interface OnQuestionAddedListener {
        void onQuestionAdded();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestionAddedListener) {
            listener = (OnQuestionAddedListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnQuestionAddedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_question, null);

        // Ánh xạ các thành phần
        etQuestion = view.findViewById(R.id.question);
        etAnswer = view.findViewById(R.id.answer);
        etA = view.findViewById(R.id.choie_a);
        etB = view.findViewById(R.id.choie_b);
        etC = view.findViewById(R.id.choie_c);
        etD = view.findViewById(R.id.choie_d);
        Button btnSubmit = view.findViewById(R.id.submit);
        Button btnClose = view.findViewById(R.id.btnClose);

        // Xử lý nút Submit
        btnSubmit.setOnClickListener(v -> handleSubmit());

        // Xử lý nút Đóng
        btnClose.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }

    // Xử lý thêm câu hỏi
    private void handleSubmit() {
        String question = etQuestion.getText().toString().trim();
        String answer = etAnswer.getText().toString().trim();
        String a = etA.getText().toString().trim();
        String b = etB.getText().toString().trim();
        String c = etC.getText().toString().trim();
        String d = etD.getText().toString().trim();

        if (question.isEmpty() || answer.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm vào database
        insertData(question, answer, a, b, c, d);
        listener.onQuestionAdded();
        dismiss();
    }

    // Thêm dữ liệu vào database
    private void insertData(String question, String answer, String a, String b, String c, String d) {
        try {
            SQLiteDatabase db = requireActivity().openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
            String sql = "INSERT INTO tblquiz (question, answer, a, b, c, d) VALUES (?, ?, ?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, question);
            statement.bindString(2, answer);
            statement.bindString(3, a);
            statement.bindString(4, b);
            statement.bindString(5, c);
            statement.bindString(6, d);
            statement.execute();

            // Hiển thị dialog xác nhận tiếp tục
            showContinueDialog();

            // Cập nhật danh sách câu hỏi
            listener.onQuestionAdded();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức hiển thị dialog tiếp tục
    private void showContinueDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Thêm câu hỏi thành công!")
                .setMessage("Bạn có muốn tiếp tục thêm câu hỏi mới?")
                .setPositiveButton("Tiếp tục", (dialog, which) -> {
                    // Xóa nội dung các trường nhập liệu
                    clearInputFields();
                })
                .setNegativeButton("Thoát", (dialog, which) -> dismiss())
                .setCancelable(false)
                .show();
    }

    // Xóa nội dung các trường nhập liệu
    private void clearInputFields() {
        etQuestion.setText("");
        etAnswer.setText("");
        etA.setText("");
        etB.setText("");
        etC.setText("");
        etD.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Thiết lập kích thước dialog
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}