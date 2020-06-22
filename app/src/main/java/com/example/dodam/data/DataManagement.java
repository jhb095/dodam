package com.example.dodam.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 앱 상 전반적인 데이터를 다루는 클래스(싱글톤)
public class DataManagement {
    private static DataManagement dataManager = new DataManagement();
    private UserData userData;                  // 유저 정보

    // 생성자
    private DataManagement() {
        userData = null;
    }

    // 객체 가져오기
    public static DataManagement getInstance() {
        return dataManager;
    }

    // UserData 설정
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    // UserData 반환
    public UserData getUserData() {
        return userData;
    }

    // 평점 순으로 내림차순 정렬
    public List<CosmeticRankItemData> sortByRate(List<CosmeticRankItemData> data) {
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
}