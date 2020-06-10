package com.example.dodam.data;

import java.util.Calendar;

public class UserData {
    private String  id;          // 사용자 id(DB상 고유 사용자 번호)
    private String  email;       // 사용자 이메일(로그인시 사용)
    private String  password;    // 사용자 비밀번호(로그인시 사용)
    private String  name;        // 사용자 이름
    private int     age;         // 사용자 나이
    private Boolean gender;      // 사용자 성별(false: 남성, true: 여성)
    private int     skinType;    // 사용자 피부타입
    private String  signUpDate;  // 사용자 가입날짜

    // 기본 생성자(Firebase)
    public UserData() {

    }

    // 생성자
    public UserData(String email, String password, String name, int age, Boolean gender) {
        Calendar calendar;

        this.email      = email;
        this.password   = password;
        this.name       = name;
        this.age        = age;
        this.gender     = gender;

        // 피부타입은 0으로 초기화
        skinType = 0;

        // 가입일 설정
        calendar = Calendar.getInstance();

        signUpDate = String.valueOf(calendar.get(Calendar.YEAR))
                + "년 "
                + String.valueOf(calendar.get(Calendar.MONTH) + 1)
                + "월 "
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))
                + "일";
    }

    // 사용자 id 설정
    public void setId(String id) {
        this.id = id;
    }

    // 사용자 id 반환
    public String getId() {
        return id;
    }

    // 사용자 email 설정
    public void setEmail(String email) {
        this.email = email;
    }

    // 사용자 email 반환
    public String getEmail() {
        return email;
    }

    // 사용자 비밀번호 설정
    public void setPassword(String password) {
        this.password = password;
    }

    // 사용자 비밀번호 반환
    public String getPassword() {
        return password;
    }

    // 사용자 이름 설정
    public void setName(String name) {
        this.name = name;
    }

    // 사용자 이름 반환
    public String getName() {
        return name;
    }

    // 사용자 나이 설정
    public void setAge(int age) {
        this.age = age;
    }

    // 사용자 나이 반환
    public int getAge() {
        return age;
    }

    // 사용자 성별 설정
    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    // 사용자 성별 반환
    public Boolean getGender() {
        return gender;
    }

    // 사용자 피부타입 설정
    public void setSkinType(int skinType) {
        this.skinType = skinType;
    }

    // 사용자 피부타입 반환
    public int getSkinType() {
        return skinType;
    }

    // 사용자 가입날짜 설정
    public void setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
    }

    // 사용자 가입날짜 반환
    public String getSignUpDate() {
        return signUpDate;
    }

    // 생년월일로부터 나이 구하기
    public static int getAgeFromBirthDay(String birthDay) {
        Calendar calendar;
        String year;
        int age;

        calendar = Calendar.getInstance();

        // 생년만 있으면 나이를 구할 수 있음
        year = birthDay.substring(0, 4);

        age = calendar.get(Calendar.YEAR) - Integer.parseInt(year) + 1;

        return age;
    }

    // 성별 String으로 부터 Boolean 값 성별로 바꿔 반환
    public static Boolean getGenderFromString(String gender) {
        if(gender.equals("남성")) {
            return Constant.MALE;
        }

        return Constant.MALE;
    }
}
