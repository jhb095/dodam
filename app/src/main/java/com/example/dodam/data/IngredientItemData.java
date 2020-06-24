package com.example.dodam.data;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class IngredientItemData implements Serializable {
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

    @Override
    public boolean equals(@Nullable Object obj) {
        IngredientItemData i;

        i = (IngredientItemData) obj;

        return ingredientName.equals(i.ingredientName);
    }
}
