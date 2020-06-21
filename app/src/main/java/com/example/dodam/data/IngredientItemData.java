package com.example.dodam.data;

public class IngredientItemData {
    private String ingredientName;    // 성분 명
    private Boolean isExist;          // DB에 존재하는지

    // 성분 명 설정
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    // 성분 명 반환
    public String getIngredientName() {
        return ingredientName;
    }

    // DB 존재 여부 설정
    public void setIsExist(Boolean isExist) {
        this.isExist = isExist;
    }

    // DB 존재 여부 반환
    public Boolean getIsExist() {
        return isExist;
    }
}
