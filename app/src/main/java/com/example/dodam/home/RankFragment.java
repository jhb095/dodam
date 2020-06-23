package com.example.dodam.home;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.dodam.R;
import com.example.dodam.data.Constant;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.data.DataManagement;
import com.google.android.gms.vision.text.Line;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Locale;

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
    }

    @Override
    public void onResume() {
        super.onResume();

        if(categoryRVAdapter == null) {
            initializeRecyclerView();
        }

        refreshCategory(Constant.CATEGORY_ALL);
        refreshSkinType(Constant.SKIN_DRY);
        refreshAge("10대 미만");
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

    // 카테고리별 항목 새로고침
    private void refreshCategory(String category) {
        List<CosmeticRankItemData> cosmetics;
        int rank;

        rank = 1;

        // 먼저 목록 지우기
        categoryRVAdapter.delAllItem();

        cosmetics = DataManagement.getInstance().getCosmetics();

        cosmetics = DataManagement.getInstance().getCosmeticFromCategory(cosmetics, category);
        cosmetics = DataManagement.getInstance().sortByCosemticRate(cosmetics);

        // 2순위 까지만 보여주기
        for(int i = 0; i < cosmetics.size(); i++) {
            if(i == 2) {
                break;
            }

            CosmeticRankItemData cosmeticRankItemData;

            cosmeticRankItemData = cosmetics.get(i);
            cosmeticRankItemData.setRank(rank++);

            categoryRVAdapter.addItem(cosmeticRankItemData);
        }

        // 변경됬음을 알림
        categoryRVAdapter.notifyDataSetChanged();
    }

    // 피부타입별 항목 새로고침
    private void refreshSkinType(String skinType) {
        List<CosmeticRankItemData> cosmetics;
        int rank;

        rank = 1;

        // 먼저 목록 지우기
        skinTypeRVAdapter.delAllItem();

        cosmetics = DataManagement.getInstance().getCosmetics();
        cosmetics = DataManagement.getInstance().getCosmeticFromSkinType(cosmetics, skinType);
        cosmetics = DataManagement.getInstance().sortByCosemticRate(cosmetics);

        // 2순위 까지만 보여주기
        for(int i = 0; i < cosmetics.size(); i++) {
            if(i == 2) {
                break;
            }

            CosmeticRankItemData cosmeticRankItemData;

            cosmeticRankItemData = cosmetics.get(i);
            cosmeticRankItemData.setRank(rank++);

            skinTypeRVAdapter.addItem(cosmeticRankItemData);
        }

        // 변경됬음을 알림
        skinTypeRVAdapter.notifyDataSetChanged();
    }

    // 연령별 항목 새로고침
    private void refreshAge(String age) {
        List<CosmeticRankItemData> cosmetics;
        int rank;

        rank = 1;

        // 먼저 목록 지우기
        ageRVAdapter.delAllItem();

        cosmetics = DataManagement.getInstance().getCosmetics();
        cosmetics = DataManagement.getInstance().getCosmeticFromAge(cosmetics, age);
        cosmetics = DataManagement.getInstance().sortByCosemticRate(cosmetics);

        // 2순위 까지만 보여주기
        for(int i = 0; i < cosmetics.size(); i++) {
            if(i == 2) {
                break;
            }

            CosmeticRankItemData cosmeticRankItemData;

            cosmeticRankItemData = cosmetics.get(i);
            cosmeticRankItemData.setRank(rank++);

            ageRVAdapter.addItem(cosmeticRankItemData);
        }

        // 변경됬음을 알림
        ageRVAdapter.notifyDataSetChanged();
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
                refreshCategory(tab.getText().toString());
                break;

            // 피부타입별
            case R.id.rank_skinTypeTabLayout:
                // 피부타입별 전체
                refreshSkinType(tab.getText().toString());
                break;

            // 연령별
            case R.id.rank_ageTabLayout:
                // 연령별 전체
                refreshAge(tab.getText().toString());
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
        LinearLayout themeLayout, reviewLayout;

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
