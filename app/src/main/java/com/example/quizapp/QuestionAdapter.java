package com.example.quizapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Question> questions;

    public QuestionAdapter(Context context, ArrayList<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        }

        Question question = questions.get(position);

        // Ánh xạ view
        TextView txtId = convertView.findViewById(R.id.txtId);
        TextView txtQuestion = convertView.findViewById(R.id.txtQuestion);
        TextView txtAnswer = convertView.findViewById(R.id.txtAnswer);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

        // Hiển thị dữ liệu
        txtId.setText("ID: " + question.id);
        txtQuestion.setText(question.question);
        txtAnswer.setText("Đáp án: " + question.answer);

        // Xử lý sự kiện chỉnh sửa - Sử dụng DialogFragment
        btnEdit.setOnClickListener(v -> {
            // Đóng gói dữ liệu vào Bundle
            Bundle args = new Bundle();
            args.putString("id", question.id);
            args.putString("question", question.question);
            args.putString("answer", question.answer);
            args.putString("a", question.a);
            args.putString("b", question.b);
            args.putString("c", question.c);
            args.putString("d", question.d);

            // Tạo và hiển thị DialogFragment
            UpdateQuestionDialogFragment dialog = new UpdateQuestionDialogFragment();
            dialog.setArguments(args);

            // Sử dụng SupportFragmentManager từ Activity
            if (context instanceof AppCompatActivity) {
                dialog.show(
                        ((AppCompatActivity) context).getSupportFragmentManager(),
                        "UpdateQuestionDialog"
                );
            }
        });

        // Xử lý sự kiện xóa
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn chắc chắn muốn xóa câu hỏi này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        ((ShowquestionActivity) context).deleteQuestion(question.id);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        return convertView;
    }
}