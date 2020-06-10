package com.example.dodam.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dodam.R;
import com.example.dodam.database.DatabaseManagement;
import com.example.dodam.database.FirebaseCallback;
import com.example.dodam.home.HomeActivity;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        ImageView exitIV;
        TextView signInTV;

        exitIV = (ImageView)findViewById(R.id.signIn_exit);
        signInTV = (TextView)findViewById(R.id.signIn_nextTV);

        // Click Listener 추가
        exitIV.setOnClickListener(this);
        signInTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            // exit
            case R.id.signIn_exit:
                finish();

                break;

            // 다음
            case R.id.signIn_nextTV:
                // 로그인
                signIn();

                break;
        }
    }

    // 로그인
    private void signIn() {
        DatabaseManagement.getInstance().signInEmail(this, "jhb095@kakao.com", "123456", new FirebaseCallback<Boolean>() {
            @Override
            public void onCallback(Boolean data) {
                // 로그인 작업 성공
                if(data) {
                } else {
                    // 실패
                }
            }
        });
    }
}
