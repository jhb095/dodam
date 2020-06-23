package com.example.dodam.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodam.R;
import com.example.dodam.data.DataManagement;
import com.example.dodam.data.ReviewItemData;

import java.util.ArrayList;

public class ReviewItemRVAdapter extends RecyclerView.Adapter<ReviewItemRVAdapter.ItemViewHolder> {
    private ArrayList<ReviewItemData> listData = new ArrayList<>(); // adapter에 들어갈 list
    private OnItemClickListener mListener = null;                  // listener 객체

    public ReviewItemRVAdapter() {
    }

    // listener interface
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    // OnItemClickListener 객체 설정
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        // LayoutInflater를 이용하여 item.xml을 inflate
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);

        return new ItemViewHolder(view);
    }

    // Item 항목 하나하나씩 bind, 즉 보여주는 메소드
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        holder.onBind(listData.get(position));
        holder.getLikeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onItemClick(v, position);
                }
            }
        });
        holder.getDislikeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onItemClick(v, position);
                }
            }
        });
    }

    // RecyclerView의 총 개수 가져오기
    @Override
    public int getItemCount() {
        return listData.size();
    }

    // 외부에서 item 항목 추가
    public void addItem(ReviewItemData data) {
        listData.add(data);
    }

    // item 항목 가져오기
    public ReviewItemData getItem(int pos) { return listData.get(pos); }

    // 외부에서 모든 item 항목 삭제
    public void delAllItem() {
        listData.clear();
    }

    // subView 셋팅
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTV, userInfoTV, writeDateTV, contentTV, likeTV, dislikeTV;
        private ImageView profileIV;
        private ImageView[] starIVs;
        private Button likeBtn, dislikeBtn;

        ItemViewHolder(View itemView) {
            super(itemView);

            userNameTV = itemView.findViewById(R.id.reviewItem_userNameTV);
            userInfoTV = itemView.findViewById(R.id.reviewItem_userInfoTV);
            writeDateTV = itemView.findViewById(R.id.reviewItem_writeDateTV);
            contentTV = itemView.findViewById(R.id.reviewItem_contentTV);
            likeTV = itemView.findViewById(R.id.reviewItem_likeCountTV);
            dislikeTV = itemView.findViewById(R.id.reviewItem_dislikeCountTV);

            starIVs = new ImageView[5];

            starIVs[0] = itemView.findViewById(R.id.reviewItem_star1);
            starIVs[1] = itemView.findViewById(R.id.reviewItem_star2);
            starIVs[2] = itemView.findViewById(R.id.reviewItem_star3);
            starIVs[3] = itemView.findViewById(R.id.reviewItem_star4);
            starIVs[4] = itemView.findViewById(R.id.reviewItem_star5);

            likeBtn = itemView.findViewById(R.id.reviewItem_likeBtn);
            dislikeBtn = itemView.findViewById(R.id.reviewItem_dislikeBtn);
        }

        void onBind(ReviewItemData data) {
            float rate;

            rate = data.getRate();

            userNameTV.setText(data.getUserName());
            userInfoTV.setText(data.getUserInfo());
            writeDateTV.setText(data.getWriteDate());
            contentTV.setText(data.getContent());
            likeTV.setText(String.valueOf(data.getLike()));
            dislikeTV.setText(String.valueOf(data.getDislike()));

            if(rate < 5) {
                starIVs[4].setVisibility(View.INVISIBLE);
            }

            if(rate < 4) {
                starIVs[3].setVisibility(View.INVISIBLE);
            }

            if(rate < 3) {
                starIVs[2].setVisibility(View.INVISIBLE);
            }

            if(rate < 2) {
                starIVs[1].setVisibility(View.INVISIBLE);
            }

            if(rate < 1) {
                starIVs[0].setVisibility(View.INVISIBLE);
            }
        }

        public Button getLikeBtn() {
            return likeBtn;
        }

        public Button getDislikeBtn() {
            return dislikeBtn;
        }
    }
}
