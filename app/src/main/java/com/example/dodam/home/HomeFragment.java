package com.example.dodam.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dodam.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        ImageView mySkinType;

        v = inflater.inflate(R.layout.fragment_home, container, false);

        mySkinType = v.findViewById(R.id.home_mySkinType);

        // Click Listener 추가
        mySkinType.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 내 피부타입은? 이미지
            case R.id.home_mySkinType:
                // MySkinTypeFragment로 이동
                ((HomeActivity)getActivity()).replaceFragment(3);

                break;
        }
    }
}
