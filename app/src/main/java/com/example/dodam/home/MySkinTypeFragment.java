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
import android.widget.TextView;

import com.example.dodam.R;
import com.example.dodam.data.Constant;
import com.example.dodam.data.DataManagement;
import com.example.dodam.data.SurveyItemData;
import com.example.dodam.data.UserData;

public class MySkinTypeFragment extends Fragment implements View.OnClickListener {
    private View root;
    private RecyclerView surveyRV;
    private SurveyItemRVAdapter surveyRVAdapter;
    private int nextQuestionNumber = 0;
    private static int[] contentResources =
            new int[]{R.array.survey_content1, R.array.survey_content2, R.array.survey_content3, R.array.survey_content4
                    , R.array.survey_content5, R.array.survey_content6, R.array.survey_content7, R.array.survey_content8
                    , R.array.survey_content9, R.array.survey_content10, R.array.survey_content11, R.array.survey_content12
                    , R.array.survey_content13, R.array.survey_content14, R.array.survey_content15};
    private double[] grades = new double[]{0, 0};    // 설문 점수(Part1, Part2)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_skin_type, container, false);

        // 필요한 항목 초기화
        initailize();

        return root;
    }

    // 필요한 항목 초기화
    private void initailize() {
        Button startBtn, goRecommendBtn;

        startBtn = root.findViewById(R.id.mySkinType_startBtn);
        goRecommendBtn = root.findViewById(R.id.mySkinType_goRecommendBtn);

        // click listener 추가
        startBtn.setOnClickListener(this);
        goRecommendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 시작하기
            case R.id.mySkinType_startBtn:
                // 설문 시작
                startSurvey();

                break;

            // 추천 받으러 가기
            case R.id.mySkinType_goRecommendBtn:
                ((HomeActivity)getActivity()).replaceFragment(2);

                break;
        }
    }

    // 설문 시작
    private void startSurvey() {
        ConstraintLayout mainLayout, surveyLayout;

        mainLayout = root.findViewById(R.id.mySkinType_mainLayout);
        surveyLayout = root.findViewById(R.id.mySkinType_surveyLayout);

        // main Layout 숨기고 survey Layout 보이기
        mainLayout.setVisibility(View.INVISIBLE);
        surveyLayout.setVisibility(View.VISIBLE);

        // 설문 항목을 보여주는 뷰 초기화
        initSurveyRecyclerView();

        // 첫 항목부터 띄우기
        refreshSurveyRecyclerView(nextQuestionNumber);
    }

    // 설문 항목을 담는 RecyclerView 초기화
    private void initSurveyRecyclerView() {
        surveyRV = root.findViewById(R.id.mySkinType_surveyRV);
        surveyRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        surveyRVAdapter = new SurveyItemRVAdapter();

        // RecyclerView Adapter에 Item Click Listener 추가
        surveyRVAdapter.setOnItemClickListener(new SurveyItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                int part;

                // 마지막 항목이라면
                if(nextQuestionNumber + 1 == Constant.maxSurveyQuestion) {
                    ConstraintLayout mainLayout, surveyLayout;
                    TextView surveyTV, surveyResultTV;
                    Button startBtn, goRecommendBtn;
                    UserData userData;

                    // 점수를 토대로 피부타입 결정
                    userData = DataManagement.getInstance().getUserData();
                    userData.setSkinTypeFromGrade(grades);

                    // Textview
                    surveyTV = root.findViewById(R.id.mySkinType_surveyTV);
                    surveyResultTV = root.findViewById(R.id.mySkinType_surveyResultTV);

                    // 텍스트 수정후 보이게
                    surveyTV.setText(R.string.survey_result);
                    surveyResultTV.setText(userData.getSkinType1() + ", " + userData.getSkinType2());
                    surveyResultTV.setVisibility(View.VISIBLE);

                    // Button
                    startBtn = root.findViewById(R.id.mySkinType_startBtn);
                    goRecommendBtn = root.findViewById(R.id.mySkinType_goRecommendBtn);

                    // '시작하기'버튼 안보이게 하고 추천받으러 가는 버튼 보이게 하기
                    startBtn.setVisibility(View.INVISIBLE);
                    goRecommendBtn.setVisibility(View.VISIBLE);

                    // Layout
                    mainLayout = root.findViewById(R.id.mySkinType_mainLayout);
                    surveyLayout = root.findViewById(R.id.mySkinType_surveyLayout);

                    // survey Layout을 안보이게 하고 main Layout을 띄우고
                    surveyLayout.setVisibility(View.INVISIBLE);
                    mainLayout.setVisibility(View.VISIBLE);

                    return;
                }

                // 첫번째 파트
                if(nextQuestionNumber < 6) {
                    part = 0;
                } else {
                    // 두번째 파트
                    part = 1;
                }

                // 각 설문에 대한 답을 골랐으면 점수를 증가시키고 다음 문항으로 넘어가야함
                switch(pos) {
                    case 0:
                        grades[part] += 1;
                        break;

                    case 1:
                        grades[part] += 2;
                        break;

                    case 2:
                        grades[part] += 3;
                        break;

                    case 3:
                        grades[part] += 4;
                        break;

                    case 4:
                        grades[part] += 2.5;
                        break;
                }

                refreshSurveyRecyclerView(++nextQuestionNumber);
            }
        });

        surveyRV.setAdapter(surveyRVAdapter);
    }

    // 설문 RecyclerView 새로고침
    private void refreshSurveyRecyclerView(int questionNumber) {
        TextView questionTitleTV, questionNumberTV;
        String[] contents; // 설문 보기 항목

        // 해당 문항에 대한 보기 가져오기
        contents = getResources().getStringArray(contentResources[questionNumber]);

        // 설문 제목 및 번호변경
        questionTitleTV = root.findViewById(R.id.mySkinType_questionTitleTV);
        questionNumberTV = root.findViewById(R.id.mySkinType_questionNumberTV);

        questionTitleTV.setText(getResources().getStringArray(R.array.surey_title)[questionNumber]);
        questionNumberTV.setText("QUESTION " + (questionNumber + 1));

        // Survey RecyclerView Item 전부 삭제
        surveyRVAdapter.delAllItem();

        for(String content : contents) {
            SurveyItemData surveyItem;

            surveyItem = new SurveyItemData();

            surveyItem.setContent(content);

            // recyclerview에 추가
            surveyRVAdapter.addItem(surveyItem);
        }

        // 변경된 항목들 보여주기
        surveyRVAdapter.notifyDataSetChanged();
    }
}
