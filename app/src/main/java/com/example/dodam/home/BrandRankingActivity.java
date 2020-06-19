package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dodam.R;

public class BrandRankingActivity extends AppCompatActivity implements View.OnClickListener, BrandItemRVAdapter.OnItemClickListener {
    private RecyclerView brandRV;
    private BrandItemRVAdapter brandItemRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_brand_ranking);

        // 필요한 항목 초기화
        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        initializeImageView();
        initializeRecyclerView();
    }

    // ImageView 초기화
    private void initializeImageView() {
        ImageView backIV;

        backIV = findViewById(R.id.brandRanking_backIV);

        backIV.setOnClickListener(this);
    }

    // RecyclerView 초기화
    private void initializeRecyclerView() {
        brandRV = findViewById(R.id.brandRanking_brandRV);

        brandRV.setLayoutManager(new LinearLayoutManager(BrandRankingActivity.this, LinearLayoutManager.VERTICAL, false));

        brandItemRVAdapter = new BrandItemRVAdapter();

        brandItemRVAdapter.setOnItemClickListener(this);

        brandRV.setAdapter(brandItemRVAdapter);
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

    // RecyclerView Item항목 클릭시
    @Override
    public void onItemClick(View v, int pos) {
        // 클릭한 브랜드 세부 정보 화면으로 넘어가기

    }
}
