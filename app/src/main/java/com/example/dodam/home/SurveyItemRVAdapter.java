package com.example.dodam.home;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodam.R;
import com.example.dodam.data.SurveyItemData;

import java.util.ArrayList;

public class SurveyItemRVAdapter extends RecyclerView.Adapter<SurveyItemRVAdapter.ItemViewHolder> {
    private ArrayList<SurveyItemData> listData = new ArrayList<>(); // adapter에 들어갈 list
    private OnItemClickListener mListener = null;                  // listener 객체
    private static String[] numbers = new String[]{"A", "B", "C", "D", "E"};
    private static int[] colors = new int[]{
            Color.rgb(246, 204, 212)
            , Color.rgb(217, 109, 130)
            , Color.rgb(109, 172, 199)
            , Color.rgb(152, 135, 166)
            , Color.rgb(94, 67, 107)};

    public SurveyItemRVAdapter() {
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);

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
    public void addItem(SurveyItemData data) {
        // 추가하기전 번호 및 색상정보 넣기
        data.setNumber(numbers[listData.size()]);
        data.setTint(ColorStateList.valueOf(colors[listData.size()]));

        listData.add(data);
    }

    // item 항목 가져오기
    public SurveyItemData getItem(int pos) { return listData.get(pos); }

    // 외부에서 모든 item 항목 삭제
    public void delAllItem() {
        listData.clear();
    }

    // subView 셋팅
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1;
        private TextView textView2;
        private ImageView leftIV;

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

            textView1 = itemView.findViewById(R.id.surveyItem_numberTV);
            textView2 = itemView.findViewById(R.id.surveyItem_contentTV);
            leftIV = itemView.findViewById(R.id.surveyItem_leftIV);
        }

        void onBind(SurveyItemData data) {
            textView1.setText(data.getNumber());
            textView1.setTextColor(data.getTint());
            textView2.setText(data.getContent());
            leftIV.setBackgroundTintList(data.getTint());
        }
    }
}
