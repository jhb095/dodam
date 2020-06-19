package com.example.dodam.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dodam.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);


        // 필요한 항목 초기화
        initialize();

        return root;
    }

    // 필요한 항목 초기화
    private void initialize() {
        initializeButton();
        initializeImageView();
    }

    // Button 초기화
    private void initializeButton() {
        Button categoryBtn, brandBtn, skinTypeBtn, ageBtn;

        categoryBtn = root.findViewById(R.id.home_categoryBtn);
        brandBtn = root.findViewById(R.id.home_brandBtn);
        skinTypeBtn = root.findViewById(R.id.home_skinTypeBtn);
        ageBtn = root.findViewById(R.id.home_ageBtn);

        // Click Listener 추가
        categoryBtn.setOnClickListener(this);
        brandBtn.setOnClickListener(this);
        skinTypeBtn.setOnClickListener(this);
        ageBtn.setOnClickListener(this);
    }

    // ImageView 초기화
    private void initializeImageView() {
        ImageView mySkinType;

        mySkinType = root.findViewById(R.id.home_mySkinType);

        // Click Listener 추가
        mySkinType.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            // 내 피부타입은? 이미지
            case R.id.home_mySkinType:
                // MySkinTypeFragment로 이동
                ((HomeActivity)getActivity()).replaceFragment(3);

                break;

            // 카테고리 별 버튼
            case R.id.home_categoryBtn:
                break;

            // 브랜드 별 버튼
            case R.id.home_brandBtn:
                intent = new Intent(getActivity(), BrandRankingActivity.class);

                startActivity(intent);

                break;

            // 피부타입 별 버튼
            case R.id.home_skinTypeBtn:
                break;

            // 연령대 별 버튼
            case R.id.home_ageBtn:
                break;
        }
    }
}
