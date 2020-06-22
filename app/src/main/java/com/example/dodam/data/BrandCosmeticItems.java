package com.example.dodam.data;

import java.util.ArrayList;
import java.util.List;

// Cloud Firestore 용 클래스
public class BrandCosmeticItems {
    private List<CosmeticRankItemData> cosmetics;

    public BrandCosmeticItems() {
        cosmetics = new ArrayList<>();
    }

    // 화장품 목록 설정
    public void setCosmetics(List<CosmeticRankItemData> cosmetics) {
        this.cosmetics = cosmetics;
    }

    // 화장품 목록 반환
    public List<CosmeticRankItemData> getCosmetics() {
        return cosmetics;
    }
}