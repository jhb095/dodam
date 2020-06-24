package com.example.dodam.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dodam.R;
import com.example.dodam.data.Constant;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.data.DataManagement;
import com.google.android.gms.vision.text.Line;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class RankFragment extends Fragment implements View.OnClickListener, TabLayout.OnTabSelectedListener, CosmeticRankItemRVAdapter.OnItemClickListener {
    private View root;
    private RecyclerView categoryRV, skinTypeRV, ageRV, reviewRV;
    private CosmeticRankItemRVAdapter categoryRVAdapter, skinTypeRVAdapter, ageRVAdapter, reviewRVAdapter;
    private final int REQUEST_COSMETIC_DETAIL = 1;

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
        TabLayout categoryTab, skinTypeTab, ageTab;

        super.onResume();

        initializeRecyclerView();

        categoryTab = root.findViewById(R.id.rank_categoryTabLayout);
        skinTypeTab = root.findViewById(R.id.rank_skinTypeTabLayout);
        ageTab = root.findViewById(R.id.rank_ageTabLayout);

        refreshCategory(categoryTab.getTabAt(categoryTab.getSelectedTabPosition()).getText().toString());
        refreshSkinType(skinTypeTab.getTabAt(skinTypeTab.getSelectedTabPosition()).getText().toString());
        refreshAge(ageTab.getTabAt(ageTab.getSelectedTabPosition()).getText().toString());
        refreshReview();
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
        ImageView noIV;
        int rank;

        rank = 1;

        // 먼저 목록 지우기
        categoryRVAdapter.delAllItem();

        cosmetics = DataManagement.getInstance().getCosmetics();
        cosmetics = DataManagement.getInstance().getCosmeticFromCategory(cosmetics, category);
        cosmetics = DataManagement.getInstance().sortByCosemticRate(cosmetics);

        noIV = root.findViewById(R.id.rank_categoryNoIV);

        if(cosmetics.size() == 0) {
            categoryRV.setVisibility(View.INVISIBLE);
            noIV.setVisibility(View.VISIBLE);
        } else {
            noIV.setVisibility(View.INVISIBLE);
            categoryRV.setVisibility(View.VISIBLE);
        }

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
        ImageView noIV;
        int rank;

        rank = 1;

        // 먼저 목록 지우기
        skinTypeRVAdapter.delAllItem();

        cosmetics = DataManagement.getInstance().getCosmetics();
        cosmetics = DataManagement.getInstance().getCosmeticFromSkinType(cosmetics, skinType);
        cosmetics = DataManagement.getInstance().sortByCosemticRate(cosmetics);

        noIV = root.findViewById(R.id.rank_skinTypeNoIV);

        if(cosmetics.size() == 0) {
            skinTypeRV.setVisibility(View.INVISIBLE);
            noIV.setVisibility(View.VISIBLE);
        } else {
            noIV.setVisibility(View.INVISIBLE);
            skinTypeRV.setVisibility(View.VISIBLE);
        }

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
        ImageView noIV;
        int rank;

        rank = 1;

        // 먼저 목록 지우기
        ageRVAdapter.delAllItem();

        cosmetics = DataManagement.getInstance().getCosmetics();
        cosmetics = DataManagement.getInstance().getCosmeticFromAge(cosmetics, age);
        cosmetics = DataManagement.getInstance().sortByCosemticRate(cosmetics);

        noIV = root.findViewById(R.id.rank_ageNoIV);

        if(cosmetics.size() == 0) {
            ageRV.setVisibility(View.INVISIBLE);
            noIV.setVisibility(View.VISIBLE);
        } else {
            noIV.setVisibility(View.INVISIBLE);
            ageRV.setVisibility(View.VISIBLE);
        }

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

    // 리뷰 베스트 항목 새로고침
    private void refreshReview() {
        List<CosmeticRankItemData> cosmetics;
        int rank;

        rank = 1;

        // 먼저 목록 지우기
        reviewRVAdapter.delAllItem();

        cosmetics = DataManagement.getInstance().getCosmetics();
        cosmetics = DataManagement.getInstance().sortByCosemticRate(cosmetics);

        for(CosmeticRankItemData cosmeticRankItemData : cosmetics) {
            cosmeticRankItemData.setRank(rank++);

            reviewRVAdapter.addItem(cosmeticRankItemData);
        }

        // 변경됬음을 알림
        reviewRVAdapter.notifyDataSetChanged();
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
        Intent intent;
        CosmeticRankItemData cosmeticRankItemData;
        ViewParent parent = v.getParent();

        cosmeticRankItemData = null;

        if (categoryRV.equals(parent)) {
            cosmeticRankItemData = categoryRVAdapter.getItem(pos);
        } else if (skinTypeRV.equals(parent)) {
            cosmeticRankItemData = skinTypeRVAdapter.getItem(pos);
        } else if (ageRV.equals(parent)) {
            cosmeticRankItemData = ageRVAdapter.getItem(pos);
        } else if (reviewRV.equals(parent)) {
            cosmeticRankItemData = reviewRVAdapter.getItem(pos);
        }

        intent = new Intent(getActivity(), CosmeticDetailActivity.class);

        // 해당 제품 정보 넘기기
        intent.putExtra("cosmeticItemData", cosmeticRankItemData);

        // 해당 제품 화면으로 넘어가기
        startActivityForResult(intent, REQUEST_COSMETIC_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_COSMETIC_DETAIL) {
            }
        }
    }
}
