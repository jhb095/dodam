package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dodam.R;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.data.DataManagement;
import com.example.dodam.data.UserData;
import com.example.dodam.database.Callback;
import com.example.dodam.database.DatabaseManagement;

import java.util.List;

public class IncorrectCosmeticActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView cosmeticRV;
    private CosmeticRankItemRVAdapter cosmeticRVAdapter;
    private final int REQUEST_ADD_INCORRECT_COSMETIC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_incorrect_cosmetic);

        // 필요한 항목 초기화
        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        initializeImageView();
        initializeRecyclerView();

        refreshCosmetic();
    }

    // ImageView 초기화
    private void initializeImageView() {
        ImageView backIV;

        backIV = findViewById(R.id.incorrectCosmetic_backIV);

        backIV.setOnClickListener(this);
    }

    // RecyclerView 초기화
    private void initializeRecyclerView() {
        final Context context;

        context = this;

        cosmeticRV = findViewById(R.id.incorrectCosmetic_cosmeticRV);

        cosmeticRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        cosmeticRVAdapter = new CosmeticRankItemRVAdapter(this);

        cosmeticRVAdapter.setOnItemClickListener(new CosmeticRankItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent;
                CosmeticRankItemData cosmeticRankItemData;
                UserData userData;

                cosmeticRankItemData = cosmeticRVAdapter.getItem(pos);
                userData = DataManagement.getInstance().getUserData();

                // 이미 맞지 않는 화장품으로 설정되어있으면 끝내기
                for(String cosmeticId : userData.getIncorrectCosmetics()) {
                    if(cosmeticRankItemData.getCosmeticId().equals(cosmeticId)) {
                        Toast.makeText(context, "이미 맞지 않는 화장품으로 설정했어요.", Toast.LENGTH_SHORT).show();

                        return;
                    }
                }

                // 다음 화면으로화장품 정보를 넘김
                intent = new Intent(IncorrectCosmeticActivity.this, AddIncorrectCosmeticActivity.class);

                intent.putExtra("cosmetic", cosmeticRankItemData);

                startActivityForResult(intent, REQUEST_ADD_INCORRECT_COSMETIC);
            }
        });

        cosmeticRV.setAdapter(cosmeticRVAdapter);
    }

    // 화장품 목록 새로고침
    private void refreshCosmetic() {
        // 먼저 전부 지우기
        cosmeticRVAdapter.delAllItem();

        // DB로부터 유저가 올린 화장품들 받아오기
        DatabaseManagement.getInstance().getUserCosmeticFromDatabase(0, new Callback<List<CosmeticRankItemData>>() {
            @Override
            public void onCallback(List<CosmeticRankItemData> data) {
                // 데이터를 갖고왔을 때만
                if(data != null) {
                    TextView cosmeticCountTV;

                    for(CosmeticRankItemData cosmeticRankItemData : data) {
                        cosmeticRankItemData.setRank(1);

                        // RecyclerView에 하나씩 추가
                        cosmeticRVAdapter.addItem(cosmeticRankItemData);
                    }

                    // 변경된 것을 알림
                    cosmeticRVAdapter.notifyDataSetChanged();

                    // 제품 수를 표시하는 TextView 변경
                    cosmeticCountTV = findViewById(R.id.incorrectCosmetic_cosmeticCountTV);
                    cosmeticCountTV.setText(data.size() + "개의 제품");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.incorrectCosmetic_backIV:
                finish();

                break;
        }
    }
}
