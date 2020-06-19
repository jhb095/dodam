package com.example.dodam.data;

public class BrandItemData {
    private int rank;               // 순위
    private String brandName;       // 브랜드 명

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
}
