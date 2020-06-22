package com.example.dodam.data;

import java.util.ArrayList;
import java.util.List;

// Cloud Firestore 용 클래스
public class CosmeticReviewItems {
    private List<ReviewItemData> reviews;

    public CosmeticReviewItems() {
        reviews = new ArrayList<>();
    }

    // 리뷰 목록 설정
    public void setReviews(List<ReviewItemData> reviews) {
        this.reviews = reviews;
    }

    // 화장품 목록 반환
    public List<ReviewItemData> getReviews() {
        return reviews;
    }
}