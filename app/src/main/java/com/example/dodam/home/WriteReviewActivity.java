package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dodam.R;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.database.Callback;
import com.example.dodam.database.DatabaseManagement;
import com.squareup.picasso.Picasso;

public class WriteReviewActivity extends AppCompatActivity implements View.OnClickListener {
    private CosmeticRankItemData cosmeticRankItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_review);

        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        Intent intent;

        // 데이터 수신
        intent = getIntent();

        cosmeticRankItemData = (CosmeticRankItemData)intent.getSerializableExtra("cosmeticRankItemData");

        initializeTextView();
        initializeImageView();
        initializeButton();
    }

    // TextView 초기화
    private void initializeTextView() {
        TextView brandNameTV, cosmeticNameTV;

        brandNameTV = findViewById(R.id.writeReview_brandNameTV);
        cosmeticNameTV = findViewById(R.id.writeReview_cosmeticNameTV);

        brandNameTV.setText(cosmeticRankItemData.getBrandName());
        cosmeticNameTV.setText(cosmeticRankItemData.getCosmeticName());

    }

    // ImageView 초기화
    private void initializeImageView() {
        final ImageView backIV, cosmeticIV;
        final Context context;

        context = this;

        backIV = findViewById(R.id.writeReview_backIV);
        cosmeticIV = findViewById(R.id.writeReview_cosmeticIV);

        DatabaseManagement.getInstance().getCosmeticImageFromStorage(cosmeticRankItemData.getBrandName(), cosmeticRankItemData.getCosmeticName()
                , new Callback<Uri>() {
                    @Override
                    public void onCallback(Uri data) {
                        if(data != null) {
                            Picasso.with(context).load(data).resize(200, 200).into(cosmeticIV);
                        }
                    }
                });

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
                registerReview();

                break;
        }
    }

    // 리뷰 등록하기
    private void registerReview() {
        RatingBar ratingBar;
        EditText reviewET;

        ratingBar = findViewById(R.id.writeReview_rate);
        reviewET = findViewById(R.id.writeReview_reviewET);

        // 평점을 선택하고 리뷰가 10글자 이상일 때만
        if(ratingBar.isSelected() && reviewET.getText().length() >= 10) {

        } else {
            Toast.makeText(this, "평점과 리뷰(10글자 이상) 모두 입력해 주세요", Toast.LENGTH_SHORT);
        }
    }
}
