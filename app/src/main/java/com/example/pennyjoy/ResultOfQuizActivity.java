package com.example.pennyjoy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultOfQuizActivity extends AppCompatActivity {
private TextView tvWithAnswer;
private ImageView imageWithResult;
private ImageButton returnToFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_of_test);
        tvWithAnswer = findViewById(R.id.tv_result);
        imageWithResult = findViewById(R.id.image_with_resultIcon);
        returnToFragments = findViewById(R.id.btn_return_to_fragments);
        Intent intent = getIntent();

        int resultOfQuiz = intent.getIntExtra("flagOfResult", 1000);

        switch (resultOfQuiz) {
            case 1:
            imageWithResult.setImageResource(R.drawable.check_bold);
            tvWithAnswer.setText(R.string.yes);
            break;
            case -1:
                imageWithResult.setImageResource(R.drawable.group_9);
                tvWithAnswer.setText(R.string.no);
                break;
            case -2:
                imageWithResult.setImageResource(R.drawable.group_9);
                tvWithAnswer.setText(R.string.mb_no);
                break;
            case 2:
                imageWithResult.setImageResource(R.drawable.check_bold);
                tvWithAnswer.setText(R.string.mb_yes);
                break;
        }

returnToFragments.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), AddGoodAndOtherActivity.class);
        startActivity(intent);
    }
});

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}