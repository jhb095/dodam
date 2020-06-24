package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

public class AddIncorrectCosmeticActivity extends AppCompatActivity implements View.OnClickListener {
    private CosmeticRankItemData cosmetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_incorrect_cosmetic);

        // 필요한 항목 초기화
        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        Intent intent;

        // 데이터 수신
        intent = getIntent();

        cosmetic = (CosmeticRankItemData)intent.getSerializableExtra("cosmetic");

        initializeTextView();
        initializeImageView();
        initializeButton();
    }

    // TextView 초기화
    private void initializeTextView() {
        TextView brandNameTV, cosmeticNameTV;

        brandNameTV = findViewById(R.id.addIncorrectCosmetic_brandNameTV);
        cosmeticNameTV = findViewById(R.id.addIncorrectCosmetic_cosmeticNameTV);

        brandNameTV.setText(cosmetic.getBrandName());
        cosmeticNameTV.setText(cosmetic.getCosmeticName());
    }

    // ImageView 초기화
    private void initializeImageView() {
        final ImageView cosmeticIV;
        ImageView backIV;
        final Context context;

        context = this;

        cosmeticIV = findViewById(R.id.addIncorrectCosmetic_cosmeticIV);
        backIV = findViewById(R.id.addIncorrectCosmetic_backIV);

        DatabaseManagement.getInstance().getCosmeticImageFromStorage(cosmetic.getBrandName(), cosmetic.getCosmeticName()
                , new Callback<Uri>() {
                    @Override
                    public void onCallback(Uri data) {
                        if(data != null) {
                            Picasso.with(context).load(data).resize(350, 350).into(cosmeticIV);
                        }
                    }
                });

        backIV.setOnClickListener(this);
    }

    // Button 초기화
    private void initializeButton() {
        Button addIncorrectCosmeticBtn;

        addIncorrectCosmeticBtn = findViewById(R.id.addIncorrectCosmetic_addIncorrectCosmeticBtn);

        addIncorrectCosmeticBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 뒤로가기
            case R.id.addIncorrectCosmetic_backIV:
                finish();

                break;

            // 추가
            case R.id.addIncorrectCosmetic_addIncorrectCosmeticBtn:
                UserData userData;
                final Context context;

                context = this;

                userData = DataManagement.getInstance().getUserData();
                userData.getIncorrectCosmetics().add(cosmetic.getCosmeticId());

                DatabaseManagement.getInstance().updateUserSkinTypeToDatabase(userData, new Callback<Boolean>() {
                    @Override
                    public void onCallback(Boolean data) {
                        if(data) {
                            Toast.makeText(context, "맞지 않는 화장품을 추가했어요.", Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);

                            finish();
                        } else {
                            Toast.makeText(context, "추가할 수 없어요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
        }
    }
}
