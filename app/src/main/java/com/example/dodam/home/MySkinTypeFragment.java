package com.example.dodam.home;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dodam.R;

public class MySkinTypeFragment extends Fragment implements View.OnClickListener {
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 시작하기
            case R.id.mySkinType_startBtn:
                ConstraintLayout mainLayout, surveyLayout;

                mainLayout = root.findViewById(R.id.mySkinType_mainLayout);
                surveyLayout = root.findViewById(R.id.mySkinType_surveyLayout);

                // main Layout 숨기고 survey Layout 보이기
                mainLayout.setVisibility(View.INVISIBLE);
                surveyLayout.setVisibility(View.VISIBLE);

                // 설문 시작
                startSurvey();

                break;
        }
    }

    // 설문 시작
    private void startSurvey() {

    }
}
