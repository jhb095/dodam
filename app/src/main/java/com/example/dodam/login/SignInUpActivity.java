package com.example.dodam.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dodam.R;
import com.example.dodam.data.IngredientDocument;
import com.example.dodam.data.IngredientItem;
import com.example.dodam.database.DatabaseManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SignInUpActivity extends AppCompatActivity implements View.OnClickListener {
    private int REQUEST_SIGNUP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        // 필요한 항목 초기화
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 필요한 항목 초기화
    private void initialize() throws IOException {
        Button signUpBtn;
        TextView signInTV;

        // 권한 요청
        requestPermission();

        signUpBtn = (Button)findViewById(R.id.signInUp_signUpBtn);
        signInTV = (TextView)findViewById(R.id.signInUp_signInTV);

        // Click Listener 추가
        signUpBtn.setOnClickListener(this);
        signInTV.setOnClickListener(this);
/*
        testAsyncTask test;
        test = new testAsyncTask();
        test.execute();
*/
    }
/*
    public class testAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                loadData();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    // csv파일에서 데이터를 읽어 모든 데이터를 등록한다.
    public void loadData() throws IOException {
        BufferedReader br;
        String lineString;
        IngredientDocument ingredients;
        int number = 0;

        ingredients = new IngredientDocument();

        InputStream is = this.getResources().openRawResource(R.raw.test);

        // csv파일 읽어오기
        br = new BufferedReader(new InputStreamReader(is));

        // 끝까지 한 줄씩 읽어오기
        while((lineString = br.readLine()) != null) {
            String[] splitedString;

            splitedString = lineString.split(",", 3);

            // 오염물질 데이터 생성
            IngredientItem ingredientItem;
            ingredientItem = new IngredientItem(splitedString[0], splitedString[1]);

            ingredients.getIngredientItems().add(ingredientItem);

            if(ingredients.getIngredientItems().size() == 100) {
                DatabaseManagement.getInstance().addIngredientToDatabase(ingredients, number++);

                ingredients = new IngredientDocument();
            }
            /*
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
/*        }

        // 버퍼 리더 닫기
        br.close();
    }
 */

    // 권한 요청
    private void requestPermission() {
        // 6.0 마쉬멜로 이상이면 권한 체크 후 요청
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                System.out.println("이미 권한 설정 완료");
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(SignInUpActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    // 권한 요청 응답
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            System.out.println("권한 설정 완료");
        }
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
