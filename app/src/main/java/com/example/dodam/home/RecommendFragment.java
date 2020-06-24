package com.example.dodam.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dodam.R;
import com.example.dodam.data.Constant;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.data.DataManagement;
import com.example.dodam.data.IngredientItem;
import com.example.dodam.data.IngredientItemData;
import com.example.dodam.data.UserData;
import com.example.dodam.database.Callback;
import com.example.dodam.database.DatabaseManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends Fragment implements View.OnClickListener, CosmeticRankItemRVAdapter.OnItemClickListener {
    private View root;
    private UserData userData;
    private RecyclerView correctCosmeticRV, incorrectCosmeticRV, incorrectIngredientRV;
    private CosmeticRankItemRVAdapter correctCosmeticRVAdapter, incorrectCosmeticRVAdapter;
    private IngredientItemRVAdapter incorrectIngredientRVAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recommend, container, false);

        return root;
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

                // 필요한 항목 초기화
                initialize();

                // 앱상 데이터에 추가
                DataManagement.getInstance().setCosmetics(data);

                refreshCorrectCosmetic();
                refreshIncorrectCosmetic();
            }
        });

        initializeRecyclerView();
    }

    // 필요한 항목 초기화
    private void initialize() {
        userData = DataManagement.getInstance().getUserData();

        initializeTextView();
        initializeButton();
    }

    // TextView 초기화
    private void initializeTextView() {
        TextView skinType1TV, skinType2TV;

        skinType1TV = root.findViewById(R.id.recommend_skinType1TV);
        skinType2TV = root.findViewById(R.id.recommend_skintype2TV);

        skinType1TV.setText(userData.getSkinType1());
        skinType2TV.setText(userData.getSkinType2());
    }

    // Button 초기화
    private void initializeButton() {
        Button addIncorrectCosmeticBtn, analysisBtn;

        addIncorrectCosmeticBtn = root.findViewById(R.id.recommend_addIncorrectCosmeticBtn);
        analysisBtn = root.findViewById(R.id.recommend_analysisBtn);

        addIncorrectCosmeticBtn.setOnClickListener(this);
        analysisBtn.setOnClickListener(this);
    }

    // RecyclerView 초기화
    private void initializeRecyclerView() {
        correctCosmeticRV = root.findViewById(R.id.recommend_correctCosmeticRV);
        incorrectCosmeticRV = root.findViewById(R.id.recommend_incorrectCosmeticRV);
        incorrectIngredientRV = root.findViewById(R.id.recommend_incorrectIngredientRV);

        correctCosmeticRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        incorrectCosmeticRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        incorrectIngredientRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        correctCosmeticRVAdapter = new CosmeticRankItemRVAdapter(getActivity());
        incorrectCosmeticRVAdapter = new CosmeticRankItemRVAdapter(getActivity());
        incorrectIngredientRVAdapter = new IngredientItemRVAdapter();

        correctCosmeticRVAdapter.setOnItemClickListener(this);
        incorrectCosmeticRVAdapter.setOnItemClickListener(this);

        correctCosmeticRV.setAdapter(correctCosmeticRVAdapter);
        incorrectCosmeticRV.setAdapter(incorrectCosmeticRVAdapter);
        incorrectIngredientRV.setAdapter(incorrectIngredientRVAdapter);
    }

    // 안 맞는 성분을 제외한 화장품 목록 새로고침
    private void refreshCorrectCosmetic() {
        final ImageView noIV;

        noIV = root.findViewById(R.id.recommend_correctNoIV);

        // 먼저 목록 지우기
        correctCosmeticRVAdapter.delAllItem();

        // 없으면 이미지뷰를 띄움
        if(userData.getIncorrectIngredients().size() == 0) {
            correctCosmeticRV.setVisibility(View.INVISIBLE);
            noIV.setVisibility(View.VISIBLE);
        } else {
            noIV.setVisibility(View.INVISIBLE);
            correctCosmeticRV.setVisibility(View.VISIBLE);

            DatabaseManagement.getInstance().getUserCorrectCosmeticFromDatabase(new Callback<List<CosmeticRankItemData>>() {
                @Override
                public void onCallback(List<CosmeticRankItemData> data) {
                    if (data != null) {
                        if(data.size() == 0) {
                            correctCosmeticRV.setVisibility(View.INVISIBLE);
                            noIV.setVisibility(View.VISIBLE);

                            return;
                        }

                        // 한 항목씩 추가
                        for (CosmeticRankItemData cosmeticRankItemData : data) {
                            cosmeticRankItemData.setRank(1);

                            correctCosmeticRVAdapter.addItem(cosmeticRankItemData);
                        }

                        // 변경됬음을 알림
                        correctCosmeticRVAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    // 안 맞았던 제품 새로고침
    private void refreshIncorrectCosmetic() {
        // 먼저 목록 지우기
        incorrectCosmeticRVAdapter.delAllItem();

        DatabaseManagement.getInstance().getUserCosmeticFromDatabase(1, new Callback<List<CosmeticRankItemData>>() {
            @Override
            public void onCallback(List<CosmeticRankItemData> data) {
                TextView incorrectCountTV;

                incorrectCountTV = root.findViewById(R.id.recommend_incorrectCosmeticCountTV);

                if(data != null) {
                    for(CosmeticRankItemData cosmeticRankItemData : data) {
                        cosmeticRankItemData.setRank(1);

                        incorrectCosmeticRVAdapter.addItem(cosmeticRankItemData);
                    }

                    // 변경됬음을 알림
                    incorrectCosmeticRVAdapter.notifyDataSetChanged();

                    incorrectCountTV.setText("누적제품 " + data.size() + "개");
                } else {
                    incorrectCountTV.setText("누적제품 0개");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 나랑 안맞았던 제품 추가
            case R.id.recommend_addIncorrectCosmeticBtn:
                Intent intent;

                intent = new Intent(getActivity(), IncorrectCosmeticActivity.class);

                startActivity(intent);

                break;

            // 분석하기
            case R.id.recommend_analysisBtn:
                // 나랑 안 맞는 제품 2개 이상일때만 가능
                if(userData.getIncorrectCosmetics().size() < 2) {
                    Toast.makeText(getActivity(), "안 맞는 제품을 2개 이상 등록해주세요.", Toast.LENGTH_SHORT).show();

                    return;
                }
                analysisCosmetic();

                break;
        }
    }

    // 안맞는 성분 추출
    private void analysisCosmetic() {
        // 먼저 모든 항목 제거
        incorrectIngredientRVAdapter.delAllItem();

        DatabaseManagement.getInstance().getUserCosmeticFromDatabase(1, new Callback<List<CosmeticRankItemData>>() {
            @Override
            public void onCallback(List<CosmeticRankItemData> data) {
                if(data != null && data.size() > 0) {
                    List<IngredientItemData> temp, incorrectIngredients;
                    List<String> ingredientNames;

                    // 먼저 한 화장품의 성분을 임시 list에 모두 집어넣고 이후에 중복되는 것을 맞지 않는 성분 list에 넣는다.
                    temp = data.get(0).getIngredients();
                    incorrectIngredients = new ArrayList<>();
                    ingredientNames = new ArrayList<>();

                    for(int i = 1; i < data.size(); i++) {
                        for(IngredientItemData ingredient : data.get(i).getIngredients()) {
                            int check = 0;

                            // 먼저 상관없는 성분이면 제외
                            for(String fineIngredient : Constant.FINE_INGREDIENTS) {
                                if(ingredient.getIngredientName().equals(fineIngredient)) {
                                    check = 1;

                                    break;
                                }
                            }

                            if(check == 1) {
                                continue;
                            }

                            // 포함하면 중복된 것이므로 맞지 않는 성분에 추가
                            if(temp.contains(ingredient)) {
                                ingredientNames.add(ingredient.getIngredientName());
                                incorrectIngredients.add(ingredient);
                            } else {
                                // 임시 list에 추가
                                temp.add(ingredient);
                            }
                        }
                    }

                    // 맞지 않는 성분 RecyclerView에 추가
                    for(IngredientItemData ingredient : incorrectIngredients) {
                        incorrectIngredientRVAdapter.addItem(ingredient);
                    }

                    // 유저 정보에 설정하고 DB 업데이트
                    userData.setIncorrectIngredients(ingredientNames);
                    DatabaseManagement.getInstance().updateUserSkinTypeToDatabase(userData, new Callback<Boolean>() {
                        @Override
                        public void onCallback(Boolean data) {
                        }
                    });

                    // 변경 됬음을 알림
                    incorrectIngredientRVAdapter.notifyDataSetChanged();

                    // 새로고침
                    refreshCorrectCosmetic();
                }
            }
        });
    }

    @Override
    public void onItemClick(View v, int pos) {

    }
}