package com.example.dodam.home;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dodam.R;
import com.google.android.material.tabs.TabLayout;

public class RankFragment extends Fragment implements View.OnClickListener, TabLayout.OnTabSelectedListener, CosmeticRankItemRVAdapter.OnItemClickListener {
    private View root;
    private RecyclerView categoryRV, skinTypeRV, ageRV, reviewRV;
    private CosmeticRankItemRVAdapter categoryRVAdapter, skinTypeRVAdapter, ageRVAdapter, reviewRVAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_rank, container, false);

        // 필요한 항목 초기화
        initialize();

        return root;
    }

    // 필요한 항목 초기화
    private void initialize() {
        initializeTabLayout();
        initializeButton();
        initializeRecyclerView();
    }

    // 모든 TabLayout 초기화
    private void initializeTabLayout() {
        TabLayout bestTabLayout, categoryTabLayout, skinTypeTabLayout, ageTabLayout;

        bestTabLayout = root.findViewById(R.id.rank_bestTabLayout);
        categoryTabLayout = root.findViewById(R.id.rank_categoryTabLayout);
        skinTypeTabLayout = root.findViewById(R.id.rank_skinTypeTabLayout);
        ageTabLayout = root.findViewById(R.id.rank_ageTabLayout);

        bestTabLayout.addOnTabSelectedListener(this);
        categoryTabLayout.addOnTabSelectedListener(this);
        skinTypeTabLayout.addOnTabSelectedListener(this);
        ageTabLayout.addOnTabSelectedListener(this);
    }

    // 모든 Button 초기화
    private void initializeButton() {
        Button categoryMoreBtn, skinTypeMoreBtn, ageMoreBtn;

        categoryMoreBtn = root.findViewById(R.id.rank_categoryMoreBtn);
        skinTypeMoreBtn = root.findViewById(R.id.rank_skinTypeMoreBtn);
        ageMoreBtn = root.findViewById(R.id.rank_ageMoreBtn);

        categoryMoreBtn.setOnClickListener(this);
        skinTypeMoreBtn.setOnClickListener(this);
        ageMoreBtn.setOnClickListener(this);
    }

    // 모든 RecyclerView 초기화
    private void initializeRecyclerView() {
        categoryRV = root.findViewById(R.id.rank_categoryRV);
        skinTypeRV = root.findViewById(R.id.rank_skinTypeRV);
        ageRV = root.findViewById(R.id.rank_ageRV);
        reviewRV = root.findViewById(R.id.rank_reviewRV);

        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        skinTypeRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ageRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        reviewRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        categoryRVAdapter = new CosmeticRankItemRVAdapter(getActivity());
        skinTypeRVAdapter = new CosmeticRankItemRVAdapter(getActivity());
        ageRVAdapter = new CosmeticRankItemRVAdapter(getActivity());
        reviewRVAdapter = new CosmeticRankItemRVAdapter(getActivity());

        categoryRVAdapter.setOnItemClickListener(this);
        skinTypeRVAdapter.setOnItemClickListener(this);
        ageRVAdapter.setOnItemClickListener(this);
        reviewRVAdapter.setOnItemClickListener(this);

        categoryRV.setAdapter(categoryRVAdapter);
        skinTypeRV.setAdapter(skinTypeRVAdapter);
        ageRV.setAdapter(ageRVAdapter);
        reviewRV.setAdapter(reviewRVAdapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // TabLayout 별로 구별
        switch(tab.parent.getId()) {
            // 테마, 리뷰별 베스트
            case R.id.rank_bestTabLayout:
                touchBestTabLayout(tab.getPosition());
                break;

            // 카테고리별
            case R.id.rank_categoryTabLayout:
                // 카테고리별 전체
                break;

            // 피부타입별
            case R.id.rank_skinTypeTabLayout:
                // 피부타입별 전체
                break;

            // 연령별
            case R.id.rank_ageTabLayout:
                // 연령별 전체
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 카테고리별 더보기
            case R.id.rank_categoryMoreBtn:
                break;

            // 피부타입별 더보기
            case R.id.rank_skinTypeMoreBtn:
                break;

            // 연령별 더보기
            case R.id.rank_ageMoreBtn:
                break;
        }
    }

    // 테마별 또는 리뷰별 레이아웃으로 변경
    private void touchBestTabLayout(int pos) {
        ConstraintLayout themeLayout, reviewLayout;

        themeLayout = root.findViewById(R.id.rank_themeLayout);
        reviewLayout = root.findViewById(R.id.rank_reviewLayout);

        // 테마별
        if(pos == 0) {
            // 테마별 레이아웃을 보여주고 리뷰 레이아웃은 숨기기
            reviewLayout.setVisibility(View.INVISIBLE);
            themeLayout.setVisibility(View.VISIBLE);
        } else {
            // 리뷰
            // 리뷰 레이아웃을 보여주고 테마별 레이아웃은 숨기기
            themeLayout.setVisibility(View.INVISIBLE);
            reviewLayout.setVisibility(View.VISIBLE);
        }
    }

    // CosmeticRankItemRV Item 항목 클릭시
    @Override
    public void onItemClick(View v, int pos) {
        // 화장품 정보로 넘어가기
    }
}
