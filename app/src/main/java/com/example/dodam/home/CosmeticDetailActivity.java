package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dodam.R;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.data.IngredientItem;
import com.example.dodam.data.IngredientItemData;
import com.example.dodam.database.Callback;
import com.example.dodam.database.DatabaseManagement;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

public class CosmeticDetailActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    private RecyclerView ingredientRV, reviewRV;
    private IngredientItemRVAdapter ingredientItemRVAdapter;
    private ReviewItemRVAdapter reviewItemRVAdapter;
    private CosmeticRankItemData cosmeticRankItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cosmetic_detail);

        // 필요한 항목 초기화
        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        Intent intent;

        // 데이터 수신
        intent = getIntent();

        cosmeticRankItemData = (CosmeticRankItemData)intent.getSerializableExtra("cosmeticItemData");

        initializeTextView();
        initializeImageView();
        initializeButton();
        initializeRecyclerView();
        initializeTabLayout();
    }

    // TextView 초기화
    private void initializeTextView() {
        TextView brandNameTV, cosmeticNameTV, rateTV, rankTV;

        brandNameTV = findViewById(R.id.cosmeticDetail_brandNameTV);
        cosmeticNameTV = findViewById(R.id.cosmeticDetail_cosmeticNameTV);
        rateTV = findViewById(R.id.cosmeticDetail_rateTV);
        rankTV = findViewById(R.id.cosmeticDetail_rankTV);

        brandNameTV.setText(cosmeticRankItemData.getBrandName());
        cosmeticNameTV.setText(cosmeticRankItemData.getCosmeticName());
        rateTV.setText(String.valueOf(cosmeticRankItemData.getRate()));
        rankTV.setText(cosmeticRankItemData.getRank() + "위");
    }

    // ImageView 초기화
    private void initializeImageView() {
        final ImageView backIV, cosmeticIV;
        final Context context;

        context = this;

        backIV = findViewById(R.id.cosmeticDetail_backIV);
        cosmeticIV = findViewById(R.id.cosmeticDetail_cosmeticIV);

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

        // 목록 새로고침
        refreshIngredients();
        refreshReviews();
    }

    // TabLayout 초기화
    private void initializeTabLayout() {
        TabLayout tabLayout;

        tabLayout = findViewById(R.id.cosmeticDetail_tabLayout);

        tabLayout.addOnTabSelectedListener(this);
    }

    // 제품성분 목록 새로고침
    private void refreshIngredients() {
        // 하나씩 추가
        for(IngredientItemData ingredient : cosmeticRankItemData.getIngredients()) {
            ingredientItemRVAdapter.addItem(ingredient);
        }

        // 변경된 것을 알림
        ingredientItemRVAdapter.notifyDataSetChanged();
    }

    // 리뷰 목록 새로고침
    private void refreshReviews() {

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

                // 화장품 데이터 넣어서 보내야함
                intent.putExtra("cosmeticRankItemData", cosmeticRankItemData);

                startActivity(intent);

                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        ConstraintLayout ingredientLayout, reviewLayout;

        ingredientLayout = findViewById(R.id.cosmeticDetail_ingredientLayout);
        reviewLayout = findViewById(R.id.cosmeticDetail_reviewLayout);

        switch(tab.getPosition()) {
            // 성분
            case 0:
                // 리뷰 레이아웃 숨기고 성분 레이아웃 띄우기
                reviewLayout.setVisibility(View.INVISIBLE);
                ingredientLayout.setVisibility(View.VISIBLE);

                break;

            // 리뷰
            case 1:
                // 성분 레이아웃 숨기고 리뷰 레이아웃 띄우기
                ingredientLayout.setVisibility(View.INVISIBLE);
                reviewLayout.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
