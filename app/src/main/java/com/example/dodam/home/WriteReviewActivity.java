package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dodam.R;

public class WriteReviewActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_review);

        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        initializeImageView();
        initializeButton();;
    }

    // ImageView 초기화
    private void initializeImageView() {
        ImageView backIV;

        backIV = findViewById(R.id.writeReview_backIV);

        backIV.setOnClickListener(this);
    }

    // Button 초기화
    private void initializeButton() {
        Button registerBtn;

        registerBtn = findViewById(R.id.writeReview_registerBtn);

        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 뒤로가기
            case R.id.writeReview_backIV:
                finish();

                break;

            // 등록
            case R.id.writeReview_registerBtn:
                finish();

                break;
        }
    }
}
