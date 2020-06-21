package com.example.dodam.data;

// 전반적으로 사용되는 상수들
public interface Constant {
    // 데이터베이스
    String DB_COLLECTION_USERS = "users";
    String DB_COLLECTION_INGREDIENTS = "ingredients";
    String DB_COLLECTION_BRANDS = "brands";
    String DB_COLLECTION_COSMETICS = "cosmetics";
    String DB_FIELD_REGISTERCOSMETICS = "registerCosmetics";

    // 성별
    boolean MALE    = false;
    boolean FEMALE  = true;

    // 설문 관련
    int maxSurveyQuestion = 15; // 설문 항목 개수

    // 피부 타입
    String SKIN_NO             = "알수없음";    // 설정 안됨
    String SKIN_DRY            = "건성";
    String SKIN_WEAK_DRY       = "약건성";
    String SKIN_WEAK_OILY      = "약지성";
    String SKIN_OILY           = "지성";

    String SKIN_SENSITIVE      = "민감성";
    String SKIN_WEAK_SENSITIVE = "약민감성";
    String SKIN_WEAK_RESISTANT = "약저항성";
    String SKIN_RESISTANT      = "저항성";
}
