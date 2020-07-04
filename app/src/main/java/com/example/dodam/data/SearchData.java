package com.example.dodam.data;

// 검색 결과 데이터를 갖는 클래스
public class SearchData {
    private String cosmeticName;        // 제품 명
    private String cosmeticURL;         // 제품 쇼핑 URL
    private String cosmeticImageURL;    // 제품 이미지 URL
    private String brandName;           // 제품 브랜드 명
    private String category;            // 제품 분류

    // 생성자
    public SearchData(String cosmeticName, String cosmeticURL, String cosmeticImageURL, String brandName, String category) {
        this.cosmeticName = cosmeticName;
        this.cosmeticURL = cosmeticURL;
        this.cosmeticImageURL = cosmeticImageURL;
        this.brandName = brandName;
        this.category = category;
    }

    // 제품명 설정
    public void setCosmeticName(String cosmeticName) {
        this.cosmeticName = cosmeticName;
    }

    // 제품명 반환
    public String getCosmeticName() {
        return cosmeticName;
    }

    // 제품 쇼핑 URL 설정
    public void setCosmeticURL(String cosmeticURL) {
        this.cosmeticURL = cosmeticURL;
    }

    // 제품 쇼핑 URL 반환
    public String getCosmeticURL() {
        return cosmeticURL;
    }

    // 제품 이미지 URL 설정
    public void setCosmeticImageURL(String cosmeticImageURL) {
        cosmeticImageURL = cosmeticImageURL;
    }

    // 제품 이미지 URL 반환
    public String getCosmeticImageURL() {
        return cosmeticImageURL;
    }

    // 제품 브랜드 명 설정
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    // 제품 브랜드 명 반환
    public String getBrandName() {
        return brandName;
    }

    // 제품 카테고리 설정
    public void setCategory(String category) {
        this.category = category;
    }

    // 제품 카테고리 반환
    public String getCategory() {
        return category;
    }
}
