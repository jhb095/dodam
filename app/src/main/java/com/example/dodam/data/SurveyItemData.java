package com.example.dodam.data;

import android.content.res.ColorStateList;

public class SurveyItemData {
    private String number;  // 설문 번호
    private String content; // 설문 문항
    private ColorStateList tint;    // leftIV 색상 및 문항 색깔

    // 설문 번호 설정
    public void setNumber(String number) {
        this.number = number;
    }

    // 설문 번호 반환
    public String getNumber() {
        return number;
    }

    // 설문 문항 설정
    public void setContent(String content) {
        this.content = content;
    }

    // 설문 문항 반환
    public String getContent() {
        return content;
    }

    // 색상 설정
    public void setTint(ColorStateList tint) {
        this.tint = tint;
    }

    // 색상 반환
    public ColorStateList getTint() {
        return tint;
    }
}
