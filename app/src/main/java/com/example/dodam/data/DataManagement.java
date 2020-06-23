package com.example.dodam.data;

import java.text.SimpleDateFormat;
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