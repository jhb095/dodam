package com.example.dodam.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodam.R;
import com.example.dodam.data.IngredientItemData;

import java.util.ArrayList;

public class IngredientItemRVAdapter extends RecyclerView.Adapter<IngredientItemRVAdapter.ItemViewHolder> {
    private ArrayList<IngredientItemData> listData = new ArrayList<>(); // adapter에 들어갈 list
    private OnItemClickListener mListener = null;                  // listener 객체

    public IngredientItemRVAdapter() {

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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);

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
    public void addItem(IngredientItemData data) {
        listData.add(data);
    }

    // item 항목 가져오기
    public IngredientItemData getItem(int pos) { return listData.get(pos); }

    // 외부에서 모든 item 항목 삭제
    public void delAllItem() {
        listData.clear();
    }

    // subView 셋팅
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientNameTV;

        ItemViewHolder(View itemView) {
            super(itemView);

            // ClickListener 설정
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos;

                    pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(v, pos);
                    }
                }
            });

            ingredientNameTV = itemView.findViewById(R.id.ingredientItem_ingredientNameTV);
        }

        void onBind(IngredientItemData data) {
            ingredientNameTV.setText(data.getIngredientName());
        }
    }
}
