package com.example.dodam.data;

import java.util.ArrayList;
import java.util.List;

public class IngredientDocument {
    private List<IngredientItem> ingredientItems;

    public IngredientDocument() {
        ingredientItems = new ArrayList<>();
    }

    // 성분 목록 설정
    public void setIngredientItems(List<IngredientItem> ingredientItems) {
        this.ingredientItems = ingredientItems;
    }

    // 성분 목록 반환
    public List<IngredientItem> getIngredientItems() {
        return ingredientItems;
    }
}
