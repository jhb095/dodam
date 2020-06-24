package com.example.dodam.data;

// 전반적으로 사용되는 상수들
public interface Constant {
    // 데이터베이스
    String DB_COLLECTION_USERS = "users";
    String DB_COLLECTION_INGREDIENTS = "ingredients";
    String DB_COLLECTION_BRANDS = "brands";
    String DB_COLLECTION_REVIEWS = "reviews";
    String DB_COLLECTION_COSMETICS = "cosmetics";
    String DB_COLLECTION_LOGO_BRAND = "logo_brand.png";
    String DB_FIELD_REGISTERCOSMETICS = "registerCosmetics";
    String DB_FIELD_REGISTERREVIEWS = "registerReviews";

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
    int SKIN_DRY_INT        = 0;
    int SKIN_WEAK_DRY_INT   = 1;
    int SKIN_WEAK_OILY_INT  = 2;
    int SKIN_OILY_INT       = 3;

    String SKIN_SENSITIVE      = "민감성";
    String SKIN_WEAK_SENSITIVE = "약민감성";
    String SKIN_WEAK_RESISTANT = "약저항성";
    String SKIN_RESISTANT      = "저항성";
    int SKIN_SENSITIVE_INT      = 0;
    int SKIN_WEAK_SENSITIVE_INT = 1;
    int SKIN_WEAK_RESISTANT_INT = 2;
    int SKIN_RESISTANT_INT      = 3;

    // 화장품 카테고리 관련
    String CATEGORY_ALL         = "전체";
    String CATEGORY_SKINCARE    = "스킨케어";
    String CATEGORY_CLEANSING   = "클렌징";
    String CATEGORY_MASKANDPACK = "마스크/팩";
    String CATEGORY_SUNCARE     = "선케어";
    String CATEGORY_BASE        = "베이스";

    // 화장품 성분 관련
    // 해당 성분은 제외 시키지 않아도 됨
     String[] FINE_INGREDIENTS = {"정제수"};
}