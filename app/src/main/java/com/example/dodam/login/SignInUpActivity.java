package com.example.dodam.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dodam.R;

public class SignInUpActivity extends AppCompatActivity implements View.OnClickListener {
    private int REQUEST_SIGNUP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        Button signUpBtn;
        TextView signInTV;

        signUpBtn = (Button)findViewById(R.id.signInUp_signUpBtn);
        signInTV = (TextView)findViewById(R.id.signInUp_signInTV);

        // Click Listener 추가
        signUpBtn.setOnClickListener(this);
        signInTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            // 회원가입
            case R.id.signInUp_signUpBtn:
                intent = new Intent(SignInUpActivity.this, SignUpActivity.class);

                startActivityForResult(intent, REQUEST_SIGNUP);

                break;

            // 로그인
            case R.id.signInUp_signInTV:
                intent = new Intent(SignInUpActivity.this, SignInActivity.class);

                startActivity(intent);

                break;
        }
    }
}
