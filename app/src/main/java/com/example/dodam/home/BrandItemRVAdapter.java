package com.example.dodam.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodam.R;
import com.example.dodam.data.BrandItemData;

import java.util.ArrayList;

public class BrandItemRVAdapter extends RecyclerView.Adapter<BrandItemRVAdapter.ItemViewHolder> {
    private ArrayList<BrandItemData> listData = new ArrayList<>(); // adapter에 들어갈 list
    private OnItemClickListener mListener = null;                  // listener 객체

    public BrandItemRVAdapter() {
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_item, parent, false);

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
    public void addItem(BrandItemData data) {
        listData.add(data);
    }

    // item 항목 가져오기
    public BrandItemData getItem(int pos) { return listData.get(pos); }

    // 외부에서 모든 item 항목 삭제
    public void delAllItem() {
        listData.clear();
    }

    // subView 셋팅
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView rankTV, brandNameTV;
        private ImageView brandIV;

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

            rankTV = itemView.findViewById(R.id.brandItem_rankTV);
            brandNameTV = itemView.findViewById(R.id.brandItem_brandNameTV);
        }

        void onBind(BrandItemData data) {
            rankTV.setText(data.getRank());
            brandNameTV.setText(data.getBrandName());
        }
    }
}
