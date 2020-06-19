package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dodam.R;

public class CosmeticDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView ingredientRV, reviewRV;
    private IngredientItemRVAdapter ingredientItemRVAdapter;
    private ReviewItemRVAdapter reviewItemRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosmetic_detail);
    }

    // 필요한 항목 초기화
    private void initialize() {
        initializeTextView();
        initializeImageView();
        initializeButton();
        initializeRecyclerView();
    }

    // TextView 초기화
    private void initializeTextView() {
        TextView brandNameTV, cosmeticNameTV;

        brandNameTV = findViewById(R.id.cosmeticDetail_brandNameTV);
        cosmeticNameTV = findViewById(R.id.cosmeticDetail_cosmeticNameTV);
    }

    // ImageView 초기화
    private void initializeImageView() {
        ImageView backIV, cosmeticIV;

        backIV = findViewById(R.id.cosmeticDetail_backIV);
        cosmeticIV = findViewById(R.id.cosmeticDetail_cosmeticIV);

        backIV.setOnClickListener(this);
    }

    // Button 초기화
    private void initializeButton() {
        Button writeReviewBtn;

        writeReviewBtn = findViewById(R.id.cosmeticDetail_writeReviewBtn);

        writeReviewBtn.setOnClickListener(this);
    }

    // RecyclerView 초기화
    private void initializeRecyclerView() {
        ingredientRV = findViewById(R.id.cosmeticDetail_ingredientRV);
        reviewRV = findViewById(R.id.cosmeticDetail_reviewRV);

        ingredientRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ingredientItemRVAdapter = new IngredientItemRVAdapter();
        reviewItemRVAdapter = new ReviewItemRVAdapter();

        ingredientRV.setAdapter(ingredientItemRVAdapter);
        reviewRV.setAdapter(reviewItemRVAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            // 뒤로가기
            case R.id.cosmeticDetail_backIV:
                finish();

                break;

            // 리뷰 작성
            case R.id.cosmeticDetail_writeReviewBtn:
                // 리뷰 작성 화면으로 넘어가기
                intent = new Intent(CosmeticDetailActivity.this, WriteReviewActivity.class);

                startActivity(intent);

                break;
        }
    }
}
