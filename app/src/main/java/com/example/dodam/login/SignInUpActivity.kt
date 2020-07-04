package com.example.dodam.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.example.dodam.R
import kotlinx.android.synthetic.main.activity_sign_in_up.*

class SignInUpActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_up)

        // 필요한 항목 초기화
        initialize()
    }

    // 필요한 항목 초기화
    private fun initialize() {
        initializeButton()
        initializeTextView()
    }

    // 버튼 초기화
    private fun initializeButton() {
        signInUp_signUpBtn.setOnClickListener(this)
    }

    // 텍스트뷰 초기화
    private fun initializeTextView() {
        signInUp_signInTV.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v.also {
            // View에 알맞는 Intent 생성
            val intent: Intent? = when(it?.id) {
                signInUp_signUpBtn.id -> Intent(this, SignUpActivity::class.java)
                signInUp_signInTV.id -> Intent(this, SignInActivity::class.java)
                else -> null
            }

            // null이 아닐 때 실행하자
            intent?.let { startActivity(intent) }
        }
    }
}