package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dodam.R;
import com.example.dodam.data.BrandItemData;
import com.example.dodam.database.Callback;
import com.example.dodam.database.DatabaseManagement;

import java.util.ArrayList;

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

        brandRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        brandItemRVAdapter = new BrandItemRVAdapter(this);

        brandItemRVAdapter.setOnItemClickListener(this);

        brandRV.setAdapter(brandItemRVAdapter);

        // 새로고침
        refreshBrands();
    }

    // 브랜드 목록 새로고침
    private void refreshBrands() {
        // DB에서 브랜드 목록 가져오기
        DatabaseManagement.getInstance().getBrandsFromDatabase(new Callback<ArrayList<BrandItemData>>() {
            @Override
            public void onCallback(ArrayList<BrandItemData> data) {
                // 데이터를 갖고왔을 때만
                if(data != null) {
                    for(BrandItemData brandItem : data) {
                        // RecyclerView에 하나씩 추가
                        brandItemRVAdapter.addItem(brandItem);
                    }

                    // 변경된 것을 알림
                    brandItemRVAdapter.notifyDataSetChanged();
                }
            }
        });
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
        Intent intent;

        intent = new Intent(BrandRankingActivity.this, BrandRankingDetailActivity.class);

        intent.putExtra("brandName", brandItemRVAdapter.getItem(pos).getBrandName());

        // 클릭한 브랜드 세부 정보 화면으로 넘어가기
        startActivity(intent);
    }
}
