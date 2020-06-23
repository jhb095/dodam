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
    private float rate;             // 별점
    private int reviewCount;        // 리뷰 개수
    private String popularSkinType1;    // 인기있는 피부타입1
    private String popularSkinType2;    // 인기있는 피부타입2
    private String popularAge;             // 인기있는 연령대
    private List<IngredientItemData> ingredients;   // 화장품 성분 목록

    // 기본 생성자(Firebase 때문)
    public CosmeticRankItemData() {
        ingredients = new ArrayList<>();

        popularSkinType1 = Constant.SKIN_NO;
        popularSkinType2 = Constant.SKIN_NO;
        popularAge = "";
    }

    public CosmeticRankItemData(String brandName, String cosmeticName, String category) {
        this.brandName = brandName;
        this.cosmeticName = cosmeticName;
        this.category = category;

        ingredients = new ArrayList<>();

        rank = 0;
        rate = 0;
        reviewCount = 0;

        popularSkinType1 = Constant.SKIN_NO;
        popularSkinType2 = Constant.SKIN_NO;
        popularAge = "";
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
    public void setRate(float rate) {
        this.rate = rate;
    }

    // 별점 반환
    public float getRate() {
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

    // 리뷰 개수 설정
    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    // 리뷰 개수 반환
    public int getReviewCount() {
        return reviewCount;
    }

    // 인기있는 피부타입1 설정
    public void setPopularSkinType1(String popularSkinType1) {
        this.popularSkinType1 = popularSkinType1;
    }

    // 인기있는 피부타입1 반환
    public String getPopularSkinType1() {
        return popularSkinType1;
    }

    // 인기있는 피부타입2 설정
    public void setPopularSkinType2(String popularSkinType2) {
        this.popularSkinType2 = popularSkinType2;
    }

    // 인기있는 피부타입2 반환
    public String getPopularSkinType2() {
        return popularSkinType2;
    }

    // 인기있는 연령대 설정
    public void setPopularAge(String popularAge) {
        this.popularAge = popularAge;
    }

    // 인기있는 연령대 반환
    public String getPopularAge() {
        return popularAge;
    }
}