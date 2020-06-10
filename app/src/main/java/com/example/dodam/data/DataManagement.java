package com.example.dodam.data;

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
}
