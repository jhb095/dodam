package com.example.dodam.data;

public class IngredientItem {
    private String name_ko; // 성분 한글 명
    private String name_en; // 성분 영문 명

    public IngredientItem(String name_ko, String name_en) {
        this.name_ko = name_ko;
        this.name_en = name_en;
    }

    // 성분 한글 명 설정
    public void setName_ko(String name_ko) {
        this.name_ko = name_ko;
    }

    // 성분 한글 명 반환
    public String getName_ko() {
        return name_ko;
    }

    // 성분 영문 명 설정
    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    // 성분 영문 명 반환
    public String getName_en() {
        return name_en;
    }
}
