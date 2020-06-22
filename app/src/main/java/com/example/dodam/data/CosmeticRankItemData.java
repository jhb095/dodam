package com.example.dodam.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CosmeticRankItemData implements Serializable {
    private String cosmeticId;      // 화장품 식별자(사용자가 올린 게시물 식별자)
    private int rank;               // 순위(별점을 토대로 설정)
    private String brandName;       // 브랜드 명
    private String cosmeticName;    // 화장품 명
    private String category;        // 화장품 카테고리
    private Double rate;            // 별점
    private List<IngredientItemData> ingredients;   // 화장품 성분 목록

    // 기본 생성자(Firebase 때문)
    public CosmeticRankItemData() {
        ingredients = new ArrayList<>();
    }

    public CosmeticRankItemData(String brandName, String cosmeticName, String category) {
        this.brandName = brandName;
        this.cosmeticName = cosmeticName;
        this.category = category;

        ingredients = new ArrayList<>();

        rank = 0;
        rate = 0.;
    }

    // 화장품 식별자 설정
    public void setCosmeticId(String cosmeticId) {
        this.cosmeticId = cosmeticId;
    }

    // 화장품 식별자 가져오기
    public String getCosmeticId() {
        return cosmeticId;
    }

    // 순위 설정
    public void setRank(int rank) {
        this.rank = rank;
    }

    // 순위 반환
    public int getRank() {
        return rank;
    }

    // 브랜드 명 설정
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    // 브랜드 명 반환
    public String getBrandName() {
        return brandName;
    }

    // 화장품 명 설정
    public void setCosmeticName(String cosmeticName) {
        this.cosmeticName = cosmeticName;
    }

    // 화장품 명 반환
    public String getCosmeticName() {
        return cosmeticName;
    }

    // 화장품 카테고리 설정
    public void setCategory(String category) {
        this.category = category;
    }

    // 화장품 카테고리 반환
    public String getCategory() {
        return category;
    }

    // 별점 설정
    public void setRate(Double rate) {
        this.rate = rate;
    }

    // 별점 반환
    public Double getRate() {
        return rate;
    }

    // 화장품 성분 목록 설정
    public void setIngredients(List<IngredientItemData> ingredients) {
        this.ingredients = ingredients;
    }

    // 화장품 성분 목록 반환
    public List<IngredientItemData> getIngredients() {
        return ingredients;
    }

    // 화장품 성분 추가
    public void addIngredient(IngredientItemData ingredientItemData) {
        ingredients.add(ingredientItemData);
    }
}