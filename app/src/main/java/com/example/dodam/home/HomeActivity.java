package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.dodam.R;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.data.DataManagement;
import com.example.dodam.database.Callback;
import com.example.dodam.database.DatabaseManagement;
import com.example.dodam.login.SignInActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeActivity extends AppCompatActivity implements CosmeticRankItemRVAdapter.OnItemClickListener {
    private ViewPager viewPager = null;
    private TabLayout tabs;
    private RecyclerView cosmeticRV;
    private CosmeticRankItemRVAdapter cosmeticRVAdapter;
    private final int REQUEST_COSMETIC_DETAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 모든 제품 다시 받아오기
        // 전체 화장품 목록을 받아와서 앱상 데이터에 추가
        DatabaseManagement.getInstance().getCosmeticsFromDatabase(new Callback<List<CosmeticRankItemData>>() {
            @Override
            public void onCallback(List<CosmeticRankItemData> data) {
                if (data == null) {
                    data = new ArrayList<>();
                }

                // 앱상 데이터에 추가
                DataManagement.getInstance().setCosmetics(data);

                if(viewPager == null) {
                    initialize();
                }
            }
        });
    }

    // 필요한 항목 초기화
    private void initialize() {
        initializeViewPager();
        initializeRecyclerView();
        initializeEditText();
    }

    // ViewPager 초기화
    private void initializeViewPager() {
        viewPager = findViewById(R.id.home_view_pager);

        tabs = findViewById(R.id.home_tabs);
        tabs.setupWithViewPager(viewPager);

        addTabs(viewPager);
    }

    // RecyclerView 초기화
    private void initializeRecyclerView() {
        cosmeticRV = findViewById(R.id.home_cosmeticRV);
        cosmeticRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        cosmeticRVAdapter = new CosmeticRankItemRVAdapter(this);

        cosmeticRVAdapter.setOnItemClickListener(this);

        cosmeticRV.setAdapter(cosmeticRVAdapter);
    }

    // EditText 초기화
    private void initializeEditText() {
        EditText searchET;

        // 검색 창
        searchET = findViewById(R.id.home_searchET);
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 직전
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력칸에 변화 발생 시
                // 모든 아이템 초기화
                cosmeticRVAdapter.delAllItem();

                // 모든 제품 탐색
                for(CosmeticRankItemData data : DataManagement.getInstance().getCosmetics()) {
                    String dataCosmeticName, dataBrandName, searchString;

                    dataCosmeticName = data.getCosmeticName().toLowerCase();
                    dataBrandName = data.getBrandName().toLowerCase();
                    searchString = s.toString().toLowerCase();

                    // 제품 명 또는 브랜드 명, 카테고리랑 비교해보기(입력한 문자열이 포함되는지)
                    if (dataCosmeticName.contains(searchString) || dataBrandName.contains(searchString) || data.getCategory().contains(searchString)) {
                        // 랭킹이 안보이게 하기 위해 0순위로 설정
                        data.setRank(0);

                        // 포함되면 검색결과에 추가
                        cosmeticRVAdapter.addItem(data);
                    }
                }

                // 변경된 값 표시
                cosmeticRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 입력 후
                // 아무 글씨도 없다면 ViewPager가 보이게
                if(s.length() == 0) {
                    cosmeticRV.setVisibility(View.INVISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                } else {
                    // 있다면 RecyclerView가 보이게
                    viewPager.setVisibility(View.INVISIBLE);
                    cosmeticRV.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // 탭 추가
    private void addTabs(ViewPager viewPager) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        sectionsPagerAdapter.addFrag(new HomeFragment());
        sectionsPagerAdapter.addFrag(new RankFragment());
        sectionsPagerAdapter.addFrag(new RecommendFragment());
        sectionsPagerAdapter.addFrag(new MySkinTypeFragment());

        viewPager.setAdapter(sectionsPagerAdapter);
    }

    // ViewPage 전환 메소드(Fragment를 전환)
    public void replaceFragment(int pos) {
        viewPager.setCurrentItem(pos);
    }

    @Override
    public void onItemClick(View v, int pos) {
        // 해당 제품 화면으로 넘어가기
        Intent intent;
        CosmeticRankItemData cosmeticRankItemData;

        intent = new Intent(HomeActivity.this, CosmeticDetailActivity.class);

        // 해당 제품 정보 넘기기
        cosmeticRankItemData = cosmeticRVAdapter.getItem(pos);

        intent.putExtra("cosmeticItemData", cosmeticRankItemData);

        // 해당 제품 화면으로 넘어가기
        startActivityForResult(intent, REQUEST_COSMETIC_DETAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_COSMETIC_DETAIL) {
                // 처리할 작업 없음
            }
        }
    }
}