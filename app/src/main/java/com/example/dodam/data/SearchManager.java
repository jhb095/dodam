package com.example.dodam.data;

import android.os.AsyncTask;

import com.example.dodam.database.Callback;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

// 네이버 검색 API를 이용하여 쇼핑몰에서 검색
public class SearchManager extends AsyncTask<String, Integer, Integer> {
    private String clientId = "cx53h_sJ6GGcfOiIoADZ";   // 애플리케이션 클라이언트 ID
    private String clientSecret = "TsnvYa9AgO"; // 애플리케이션 클라이언트 Secret
    private static SearchData searchCosmetic;  // 제품의 검색 결과 데이터
    private Callback<Boolean> callback;

    public SearchManager() {
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String apiURL;                          // api 요청 URL
        Map<String, String> requestHeaders;     // 요청 헤더
        String responseBody;                    // 응답 결과

        // 검색 문자열 재구성(각 공백기준으로 , 삽입)
        strings[0] = strings[0].replaceAll(" ", ", ");

        while(true) {
            try {
                System.out.println("검색 문자열: " + strings[0]);

                URLEncoder.encode(strings[0], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("검색어 인코딩 실패", e);
            }

            apiURL = "https://openapi.naver.com/v1/search/shop?query=" + strings[0] + "&display=1";

            requestHeaders = new HashMap<>();

            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);

            responseBody = get(apiURL, requestHeaders);

            // 검색결과가 없으면
            if (responseBody == null) {
                // 검색 문자열을 ", " 기준으로 뒤에서 하나 떼기
                if(strings[0].lastIndexOf(",") == -1) {
                    // 전부 잘라냈으므로 빠져나오기
                    callback.onCallback(false);

                    break;
                }

                strings[0] = strings[0].substring(0, strings[0].lastIndexOf(","));
            } else {
                callback.onCallback(true);

                // 찾았으면 빠져나오기
                break;
            }
        }

        System.out.println("응답 결과");
        System.out.println(responseBody);

        cancel(true);

        return null;
    }

    // searchText로 검색 하기
    public void search(String searchText, Callback<Boolean> callback) {
        this.callback = callback;

        execute(searchText);
    }

    // 요청에 대한 응답 받아오기
    private static String get(String apiURL, Map<String, String> requestHeaders) {
        HttpURLConnection con;  // 연결 설정 객체

        con = connect(apiURL);

        try {
            int responseCode;   // 응답 코드

            con.setRequestMethod("GET");

            for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            responseCode = con.getResponseCode();

            // 정상적으로 호출 되었을 때
            if(responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    // 연결 설정
    private static HttpURLConnection connect(String apiURL) {
        try {
            URL url;

            url = new URL(apiURL);

            return (HttpURLConnection)url.openConnection();
        } catch(MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiURL, e);
        } catch(IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiURL, e);
        }
    }

    // 응답 스트림 읽기
    private static String readBody(InputStream body) {
        InputStreamReader streamReader;

        streamReader = new InputStreamReader(body);

        try(BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody;
            String line;

            responseBody = new StringBuilder();

            while((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            // JSON에서 화장품 데이터 추출
            extractCosmeticData(responseBody);

            // 검색 결과가 없으면
            if(searchCosmetic == null) {
                return null;
            }

            return responseBody.toString();
        } catch(IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    // 제품 데이터 추출
    private static void extractCosmeticData(StringBuilder responseBody) {
        JsonParser parser;
        JsonObject jsonObj;
        JsonArray jsonArray;
        Object obj;
        String cosmeticName;
        String cosmeticURL;
        String cosmeticImageURL;
        String brandName;
        String category;
        parser = new JsonParser();
        int index;
        String preText, postText;

        obj = parser.parse(responseBody.toString());
        jsonObj = (JsonObject)obj;

        jsonArray = (JsonArray)jsonObj.get("items");

        // 검색 결과가 없으면
        if(jsonArray.size() == 0) {
            searchCosmetic = null;

            return;
        }

        jsonObj = (JsonObject)jsonArray.get(0);

        /*
        "title" 제품명
        "link" 해당 제품 쇼핑 페이지
        "image" 해당 제품 이미지
        "lprice" 해당 제품 가격
        "brand" 브랜드명
        "category2" 카테고리 중분류
         */

        cosmeticName        = jsonObj.get("title").getAsString();
        cosmeticURL         = jsonObj.get("link").getAsString();
        cosmeticImageURL    = jsonObj.get("image").getAsString();
        brandName           = jsonObj.get("brand").getAsString();
        category            = jsonObj.get("category2").getAsString();

        // HTML 태그 제거
        cosmeticName = cosmeticName.replaceAll("<b>", "");
        cosmeticName = cosmeticName.replaceAll("</b>", "");

        // 제품 명에서 '('로 시작해서 ')'로 끝나는 문자 제거
        while((index = cosmeticName.indexOf('(')) != -1) {
            preText = cosmeticName.substring(0, index);
            index = cosmeticName.indexOf(')');
            postText = cosmeticName.substring(index + 1);

            cosmeticName = preText + postText;
        }

        searchCosmetic = new SearchData(cosmeticName, cosmeticURL, cosmeticImageURL, brandName, category);
    }

    // 검색한 화장품의 데이터를 반환
    public SearchData getSearchCosmetic() {
        return searchCosmetic;
    }
}