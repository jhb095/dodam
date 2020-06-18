package com.example.dodam.data;

public class CosmeticRankItemData {
    private int rank;               // 순위
    private String brandName;       // 브랜드 명
    private String cosmeticName;    // 화장품 명
    private Double rate;            // 별점

    // 순위 설정
    public void setRank(int rank) {
        this.rank = rank;
    }

    // 순위 반환
    public int getRank() {
        return rank;
    }

    // 브랜드 명 설정
    public void setBrandName(String branName) {
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

    // 별점 설정
    public void setRate(Double rate) {
        this.rate = rate;
    }

    // 별점 반환
    public Double getRate() {
        return rate;
    }
}
