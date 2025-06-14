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

public class UpdateQuestionDialogFragment extends DialogFragment {
    private EditText etQuestion, etAnswer, etA, etB, etC, etD, etId;
    private Button btnUpdate, btnClear;
    private OnQuestionUpdatedListener listener;

    // Interface để cập nhật danh sách sau khi sửa
    public interface OnQuestionUpdatedListener {
        void onQuestionUpdated();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnQuestionUpdatedListener) {
            listener = (OnQuestionUpdatedListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnQuestionUpdatedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_question, null);

        // Ánh xạ các thành phần
        etId = view.findViewById(R.id.id);
        etQuestion = view.findViewById(R.id.question);
        etAnswer = view.findViewById(R.id.answer);
        etA = view.findViewById(R.id.ca);
        etB = view.findViewById(R.id.cb);
        etC = view.findViewById(R.id.cc);
        etD = view.findViewById(R.id.cd);
        btnUpdate = view.findViewById(R.id.submit);
        btnClear = view.findViewById(R.id.clear);

        // Nhận dữ liệu từ Adapter
        Bundle args = getArguments();
        if (args != null) {
            etId.setText(args.getString("id"));
            etQuestion.setText(args.getString("question"));
            etAnswer.setText(args.getString("answer"));
            etA.setText(args.getString("a"));
            etB.setText(args.getString("b"));
            etC.setText(args.getString("c"));
            etD.setText(args.getString("d"));
        }

        // Xử lý nút Cập nhật
        btnUpdate.setOnClickListener(v -> handleUpdate());

        // Xử lý nút Xóa
        btnClear.setOnClickListener(v -> clearFields());

        builder.setView(view);
        return builder.create();
    }

    // Xử lý cập nhật câu hỏi
    private void handleUpdate() {
        String id = etId.getText().toString().trim();
        String question = etQuestion.getText().toString().trim();
        String answer = etAnswer.getText().toString().trim();
        String a = etA.getText().toString().trim();
        String b = etB.getText().toString().trim();
        String c = etC.getText().toString().trim();
        String d = etD.getText().toString().trim();

        if (id.isEmpty() || question.isEmpty() || answer.isEmpty() ||
                a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        updateQuestion(id, question, answer, a, b, c, d);
        listener.onQuestionUpdated(); // Gọi callback
        dismiss(); // Đóng dialog
    }

    // Cập nhật dữ liệu vào database
    private void updateQuestion(String id, String question, String answer, String a, String b, String c, String d) {
        try {
            SQLiteDatabase db = requireActivity().openOrCreateDatabase("QuizDB", Context.MODE_PRIVATE, null);
            String sql = "UPDATE tblquiz SET question=?, answer=?, a=?, b=?, c=?, d=? WHERE id=?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, question);
            statement.bindString(2, answer);
            statement.bindString(3, a);
            statement.bindString(4, b);
            statement.bindString(5, c);
            statement.bindString(6, d);
            statement.bindString(7, id);
            statement.execute();
            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            listener.onQuestionUpdated(); // Gọi callback
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Xóa nội dung các trường
    private void clearFields() {
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
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}