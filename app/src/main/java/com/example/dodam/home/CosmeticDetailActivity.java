package com.example.dodam.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.example.dodam.data.ReviewItemData;
import com.example.dodam.data.UserData;
import com.example.dodam.database.Callback;
import com.example.dodam.database.DatabaseManagement;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CosmeticDetailActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener, ReviewItemRVAdapter.OnItemClickListener {
    private RecyclerView ingredientRV, reviewRV;
    private IngredientItemRVAdapter ingredientItemRVAdapter = null;
    private ReviewItemRVAdapter reviewItemRVAdapter = null;
    private CosmeticRankItemData cosmeticRankItemData;
    private final int REQUEST_WRITE_REVIEW  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cosmetic_detail);

        // 필요한 항목 초기화
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(ingredientItemRVAdapter == null && reviewItemRVAdapter == null) {
            initializeRecyclerView();
        }

        refreshReviews();
        refreshIngredients();
    }

    // 필요한 항목 초기화
    private void initialize() {
        Intent intent;

        // 데이터 수신
        intent = getIntent();

        cosmeticRankItemData = (CosmeticRankItemData)intent.getSerializableExtra("cosmeticItemData");

        initializeTextView();
        initializeImageView();
        initializeButton();
        initializeTabLayout();
    }

    // TextView 초기화
    private void initializeTextView() {
        TextView brandNameTV, cosmeticNameTV, rateTV, rankTV;

        brandNameTV = findViewById(R.id.cosmeticDetail_brandNameTV);
        cosmeticNameTV = findViewById(R.id.cosmeticDetail_cosmeticNameTV);
        rateTV = findViewById(R.id.cosmeticDetail_rateTV);
        rankTV = findViewById(R.id.cosmeticDetail_rankTV);

        brandNameTV.setText(cosmeticRankItemData.getBrandName());
        cosmeticNameTV.setText(cosmeticRankItemData.getCosmeticName());
        rateTV.setText(String.valueOf(cosmeticRankItemData.getRate()));
        rankTV.setText(cosmeticRankItemData.getRank() + "위");
    }

    // ImageView 초기화
    private void initializeImageView() {
        final ImageView backIV, cosmeticIV;
        final Context context;

        context = this;

        backIV = findViewById(R.id.cosmeticDetail_backIV);
        cosmeticIV = findViewById(R.id.cosmeticDetail_cosmeticIV);

        DatabaseManagement.getInstance().getCosmeticImageFromStorage(cosmeticRankItemData.getBrandName(), cosmeticRankItemData.getCosmeticName()
                , new Callback<Uri>() {
                    @Override
                    public void onCallback(Uri data) {
                        if(data != null) {
                            Picasso.with(context).load(data).resize(200, 200).into(cosmeticIV);
                        }
                    }
                });

        backIV.setOnClickListener(this);
    }

    // Button 초기화
    private void initializeButton() {
        Button writeReviewBtn;

        writeReviewBtn = findViewById(R.id.cosmeticDetail_writeReviewBtn);

        writeReviewBtn.setOnClickListener(this);
    }

    // RecyclerView 초기화
    private void initializeRecyclerView() {
        ingredientRV = findViewById(R.id.cosmeticDetail_ingredientRV);
        reviewRV = findViewById(R.id.cosmeticDetail_reviewRV);

        ingredientRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ingredientItemRVAdapter = new IngredientItemRVAdapter();
        reviewItemRVAdapter = new ReviewItemRVAdapter();

        reviewItemRVAdapter.setOnItemClickListener(this);

        ingredientRV.setAdapter(ingredientItemRVAdapter);
        reviewRV.setAdapter(reviewItemRVAdapter);
    }

    // TabLayout 초기화
    private void initializeTabLayout() {
        TabLayout tabLayout;

        tabLayout = findViewById(R.id.cosmeticDetail_tabLayout);
        tabLayout.getTabAt(1).setText("리뷰(" + cosmeticRankItemData.getReviewCount() + ")");

        tabLayout.addOnTabSelectedListener(this);
    }

    // 제품성분 목록 새로고침
    private void refreshIngredients() {
        // 전부 삭제
        ingredientItemRVAdapter.delAllItem();

        // 하나씩 추가
        for(IngredientItemData ingredient : cosmeticRankItemData.getIngredients()) {
            ingredientItemRVAdapter.addItem(ingredient);
        }

        // 변경된 것을 알림
        ingredientItemRVAdapter.notifyDataSetChanged();
    }

    // 리뷰 목록 새로고침
    private void refreshReviews() {
        // 전부 삭제
        reviewItemRVAdapter.delAllItem();

        // 리뷰 목록 불러오기
        DatabaseManagement.getInstance().getCosmeticReviewsFromDatabase(cosmeticRankItemData.getCosmeticId(), new Callback<List<ReviewItemData>>() {
            @Override
            public void onCallback(List<ReviewItemData> data) {
                if(data != null) {
                    TabLayout tabLayout;
                    TextView rateTV;
                    float rate;

                    tabLayout = findViewById(R.id.cosmeticDetail_tabLayout);
                    tabLayout.getTabAt(1).setText("리뷰(" + data.size() + ")");

                    rateTV = findViewById(R.id.cosmeticDetail_rateTV);

                    rate = 0;

                    // 하나씩 추가
                    for (ReviewItemData review : data) {
                        rate += review.getRate();
                        reviewItemRVAdapter.addItem(review);
                    }

                    rate /= data.size();

                    rateTV.setText(String.valueOf(rate));

                    // 변경된 것을 알림
                    reviewItemRVAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            // 뒤로가기
            case R.id.cosmeticDetail_backIV:
                finish();

                break;

            // 리뷰 작성
            case R.id.cosmeticDetail_writeReviewBtn:
                UserData userData;

                // 피부타입 설문을 안했으면 작성 불가
                if(DataManagement.getInstance().getUserData().getSkinType1().equals(Constant.SKIN_NO)) {
                    Toast.makeText(this, "피부타입을 먼저 설정하세요.", Toast.LENGTH_SHORT).show();

                    return;
                }

                userData = DataManagement.getInstance().getUserData();

                // 이미 해당 사용자가 리뷰를 작성했으면 작성할 수 없음
                for(String reviewId : userData.getRegisterReviews()) {
                    if(reviewId.equals(cosmeticRankItemData.getCosmeticId())) {
                        Toast.makeText(this, "이미 리뷰를 작성했어요.", Toast.LENGTH_SHORT).show();

                        return;
                    }
                }
                // 리뷰 작성 화면으로 넘어가기
                intent = new Intent(CosmeticDetailActivity.this, WriteReviewActivity.class);

                // 화장품 데이터 넣어서 보내야함
                intent.putExtra("cosmeticRankItemData", cosmeticRankItemData);

                startActivityForResult(intent, REQUEST_WRITE_REVIEW);

                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LinearLayout ingredientLayout, reviewLayout;

        ingredientLayout = findViewById(R.id.cosmeticDetail_ingredientLayout);
        reviewLayout = findViewById(R.id.cosmeticDetail_reviewLayout);

        switch(tab.getPosition()) {
            // 성분
            case 0:
                // 리뷰 레이아웃 숨기고 성분 레이아웃 띄우기
                reviewLayout.setVisibility(View.INVISIBLE);
                ingredientLayout.setVisibility(View.VISIBLE);

                break;

            // 리뷰
            case 1:
                // 성분 레이아웃 숨기고 리뷰 레이아웃 띄우기
                ingredientLayout.setVisibility(View.INVISIBLE);
                reviewLayout.setVisibility(View.VISIBLE);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_WRITE_REVIEW) {
                // 처리할 작업 없음
            }
        }
    }

    // Review 아이템에서 좋아요 또는 싫어요 버튼 클릭
    @Override
    public void onItemClick(final View v, int pos) {
        ReviewItemData item;
        UserData userData;
        final Context context;

        context = this;

        userData = DataManagement.getInstance().getUserData();
        item = reviewItemRVAdapter.getItem(pos);

        // 자신이 쓴 리뷰에는 누를 수 없음
        if(userData.getId().equals(item.getUserId())) {
            Toast.makeText(this, "자신이 쓴 리뷰에는 누를 수 없어요.", Toast.LENGTH_SHORT).show();

            return;
        }

        // 이미 눌렀던 리뷰에는 누를 수 없음
        for(String userId : item.getPushedLikeDislikeUsers()) {
            if(userData.getId().equals(userId)) {
                Toast.makeText(this, "이미 눌렀어요.", Toast.LENGTH_SHORT).show();

                return;
            }
        }

        // 좋아요 또는 싫어요 업데이트
        if(v.getId() == R.id.reviewItem_likeBtn) {
            TextView likeCount;

            item.setLike(item.getLike() + 1);

            likeCount = ((TextView)((View)((View) v.getParent()).findViewById(R.id.reviewItem_likeCountTV)));
            likeCount.setText(String.valueOf(item.getLike()));
        } else {
            TextView dislikeCount;

            item.setDislike(item.getDislike() + 1);

            dislikeCount = ((TextView)((View)((View) v.getParent()).findViewById(R.id.reviewItem_dislikeCountTV)));
            dislikeCount.setText(String.valueOf(item.getDislike()));
        }

        // 누른 목록에 해당 유저 추가
        item.getPushedLikeDislikeUsers().add(userData.getId());

        // DB에 리뷰 업데이트
        DatabaseManagement.getInstance().updateCosmeticReviewLikeToDatabase(item, cosmeticRankItemData.getCosmeticId(), new Callback<Boolean>() {
            @Override
            public void onCallback(Boolean data) {
                if(data) {
                    if (v.getId() == R.id.reviewItem_likeBtn) {
                        Toast.makeText(context, "좋아요를 눌렀어요.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "싫어요를 눌렀어요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "문제가 발생했어요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}