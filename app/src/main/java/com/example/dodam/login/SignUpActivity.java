package com.example.dodam.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dodam.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();
    }

    // 필요한 항목 초기화
    private void initialize() {
        ImageView exitIV;
        TextView signUpTV;

        exitIV = (ImageView)findViewById(R.id.signUp_exit);
        signUpTV = (TextView)findViewById(R.id.signUp_completeTV);

        // Click Listener 추가
        exitIV.setOnClickListener(this);
        signUpTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            // exit
            case R.id.signUp_exit:
                break;

            // 완료
            case R.id.signUp_completeTV:
                break;
        }
    }
}
