package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dodam.R;

public class BrandRankingDetailActivity extends AppCompatActivity implements View.OnClickListener, CosmeticRankItemRVAdapter.OnItemClickListener {
    private RecyclerView cosmeticRV;
    private CosmeticRankItemRVAdapter cosmeticRankItemRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_brand_ranking_detail);

        initailize();
    }

    // 필요한 항목 초기화
    private void initailize() {
        initializeTextView();
        initializeImageView();
        initializeRecyclerView();
    }

    // TextView 초기화
    private void initializeTextView() {
        TextView brandNameTV;

        brandNameTV = findViewById(R.id.brandRankingDetail_brandNameTV);
    }

    // ImageView 초기화
    private void initializeImageView() {
        ImageView backIV;
        ImageView brandIV;

        backIV = findViewById(R.id.brandRankingDetail_backIV);
        brandIV = findViewById(R.id.brandRankingDetail_brandIV);

        backIV.setOnClickListener(this);
    }

    // RecyclerView 초기화
    private void initializeRecyclerView() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 뒤로가기
            case R.id.brandRanking_backIV:
                finish();

                break;
        }
    }

    // RecyclerView Item 클릭시
    @Override
    public void onItemClick(View v, int pos) {
        // 해당 제품 화면으로 넘어가기
    }
}
