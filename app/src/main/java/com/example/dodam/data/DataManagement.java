package com.example.dodam.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// 앱 상 전반적인 데이터를 다루는 클래스(싱글톤)
public class DataManagement {
    private static DataManagement dataManager = new DataManagement();
    private UserData userData;                  // 유저 정보
    private List<CosmeticRankItemData> cosmetics;   // 전체 화장품 목록

    // 생성자
    private DataManagement() {
        userData = null;
        cosmetics = new ArrayList<>();
    }

    // 객체 가져오기
    public static DataManagement getInstance() {
        return dataManager;
    }

    // 유저 데이터 설정
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    // 유저 데이터 반환
    public UserData getUserData() {
        return userData;
    }

    // 전체 화장품 목록 설정
    public void setCosmetics(List<CosmeticRankItemData> cosmetics) {
        this.cosmetics = cosmetics;
    }

    // 전체 화장품 목록 반환
    public List<CosmeticRankItemData> getCosmetics() {
        return cosmetics;
    }

    // 화장품 평점 순으로 내림차순 정렬
    public List<CosmeticRankItemData> sortByCosemticRate(List<CosmeticRankItemData> data) {
        Collections.sort(data, new Comparator<CosmeticRankItemData>() {
            @Override
            public int compare(CosmeticRankItemData o1, CosmeticRankItemData o2) {
                if(o1.getRate() > o2.getRate()) {
                    return -1;
                } else if(o1.getRate() < o2.getRate()) {
                    return 1;
                }

                return 0;
            }
        });

        return data;
    }

    // 화장품 특정 카테고리만 뽑아서 목록 반환
    public List<CosmeticRankItemData> getCosmeticFromCategory(List<CosmeticRankItemData> data, String category) {
        List<CosmeticRankItemData> list;

        list = new ArrayList<>();

        for(CosmeticRankItemData item : data) {
            if(category.equals(Constant.CATEGORY_ALL)) {
                list.add(item);
                continue;
            }

            if(item.getCategory().equals(category)) {
                list.add(item);
            }
        }

        return list;
    }

    // 화장품 리뷰중 가장 많은 피부타입이 사용한 화장품 목록 반환
    public List<CosmeticRankItemData> getCosmeticFromSkinType(List<CosmeticRankItemData> data, String skinType) {
        List<CosmeticRankItemData> list;

        list = new ArrayList<>();

        for(CosmeticRankItemData item : data) {
            if(item.getPopularSkinType1().equals(skinType) || item.getPopularSkinType2().equals(skinType)) {
                list.add(item);
            }
        }

        return list;
    }

    // 화장품 리뷰중 가장 많은 연령대가 사용한 화장품 목록 반환
    public List<CosmeticRankItemData> getCosmeticFromAge(List<CosmeticRankItemData> data, String age) {
        List<CosmeticRankItemData> list;

        list = new ArrayList<>();

        for(CosmeticRankItemData item : data) {
            if(item.getPopularAge().equals(age)) {
                list.add(item);
            }
        }

        return list;
    }

    // 나이를 받아 'xx대'로 돌려주는 메소드
    public String convertAge(int age) {
        String ageStr;

        if(age >= 40) {
            ageStr = "40대 이상";
        } else if(age >= 30) {
            ageStr = "30대";
        } else if(age >= 20) {
            ageStr = "20대";
        } else if(age >= 10) {
            ageStr = "10대";
        } else {
            ageStr = "10대 미만";
        }

        return ageStr;
    }

    // 오늘날짜 가져오기
    public String getTodayDate() {
        Date currentDate;
        String dateStr;

        currentDate = Calendar.getInstance().getTime();

        dateStr = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(currentDate);

        return dateStr;
    }
}