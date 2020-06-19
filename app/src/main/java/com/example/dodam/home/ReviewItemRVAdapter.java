package com.example.dodam.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodam.R;
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cosmetic_rank_item, parent, false);

        return new ItemViewHolder(view);
    }

    // Item 항목 하나하나씩 bind, 즉 보여주는 메소드
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
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

        ItemViewHolder(View itemView) {
            super(itemView);

            // ClickListener 설정
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos;

                    pos = getAdapterPosition();

                    // listener 객체의 메서드 호출
                    if(pos != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(v, pos);
                    }
                }
            });

            userNameTV = itemView.findViewById(R.id.reviewItem_userNameTV);
            userInfoTV = itemView.findViewById(R.id.reviewItem_userInfoTV);
            writeDateTV = itemView.findViewById(R.id.reviewItem_writeDateTV);
            contentTV = itemView.findViewById(R.id.reviewItem_contentTV);
            likeTV = itemView.findViewById(R.id.reviewItem_likeCountTV);
            dislikeTV = itemView.findViewById(R.id.reviewItem_dislikeCountTV);
        }

        void onBind(ReviewItemData data) {
            userNameTV.setText(data.getUserName());
            userInfoTV.setText(data.getUserInfo());
            writeDateTV.setText(data.getWriteDate());
            contentTV.setText(data.getContent());
            likeTV.setText(data.getLike());
            dislikeTV.setText(data.getDislike());
        }
    }
}
