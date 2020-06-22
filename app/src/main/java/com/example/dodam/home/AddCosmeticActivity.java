package com.example.dodam.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dodam.R;
import com.example.dodam.data.Constant;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.data.IngredientItem;
import com.example.dodam.data.IngredientItemData;
import com.example.dodam.database.Callback;
import com.example.dodam.database.DatabaseManagement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AddCosmeticActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView ingredientRV;
    private IngredientItemRVAdapter ingredientItemRVAdapter;
    private final int REQUEST_CAPTURE_IMAGE = 1;
    private final int REQUEST_CAPTURE_INGREDENT = 2;
    private final int REQUEST_CAPTURE_CROP = 3;
    private String currentPhotoPath;
    private Bitmap cosmeticBitmap = null;  // 제품 사진 Bitmap
    private String selectedTexts;   // Dialog에서 선택한 문자열
    private ArrayList<String> ingredients;  // 추출한 성분

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_cosmetic);

        // 필요한 항목 초기화
        initialize();
    }

    public class parsingAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1471057/FcssReportPrdlstInforService/getReportItemList"); /*URL*/
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=uiUB1CijP3FPRbVz%2B0JCzKoB90rQvVWrfcKYgCvJuSvH3jXAcUtYjsJHb%2FM2Jbt%2FwL8BYOTRfn12j5jDsT58Mg%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("item_seq","UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /*품목일련번호*/
                urlBuilder.append("&" + URLEncoder.encode("item_name","UTF-8") + "=" + URLEncoder.encode("자오연자하거진액", "UTF-8")); /*품목명*/
                urlBuilder.append("&" + URLEncoder.encode("cosmetic_report_seq","UTF-8") + "=" + URLEncoder.encode("2008000103", "UTF-8")); /*보고일련번호*/
                urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
                urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("3", "UTF-8")); /*한 페이지 결과수*/

                URL url = new URL(urlBuilder.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                conn.setRequestProperty("Content-type", "application/json");

                conn.connect();

                System.out.println("Response code: " + conn.getResponseCode());
                BufferedReader rd;
                if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();
                System.out.println("성공: " + sb.toString());
            } catch (IOException e) {
                System.out.println("실패");
                e.printStackTrace();
            }

            return null;
        }
    }

    // 필요한 항목 초기화
    private void initialize() {
        initializeImageView();
        initializeButton();
        initializeEditText();
        initializeRecyclerView();

        //parsingAsyncTask asyncTask = new parsingAsyncTask();
        //asyncTask.execute();
    }

    // ImageView 초기화
    private void initializeImageView() {
        ImageView backIV, addCosmeticIV;

        backIV = findViewById(R.id.addCosmetic_backIV);
        addCosmeticIV = findViewById(R.id.addCosmetic_addCosmeticImageIV);

        backIV.setOnClickListener(this);
        addCosmeticIV.setOnClickListener(this);
    }

    // Button 초기화
    private void initializeButton() {
        Button completeBtn, captureIngredientBtn;

        completeBtn = findViewById(R.id.addCosmetic_completeBtn);
        captureIngredientBtn = findViewById(R.id.addCosmetic_captureIngredientBtn);

        completeBtn.setOnClickListener(this);
        captureIngredientBtn.setOnClickListener(this);
    }

    // EditText 초기화
    private void initializeEditText() {
        EditText brandNameET, cosmeticNameET;

        brandNameET = findViewById(R.id.addCosmetic_brandNameET);
        cosmeticNameET = findViewById(R.id.addCosmetic_cosmeticNameET);

        // 비활성화
        brandNameET.setClickable(false);
        brandNameET.setFocusable(false);

        cosmeticNameET.setClickable(false);
        cosmeticNameET.setFocusable(false);
    }

    // RecyclerView 초기화
    private void initializeRecyclerView() {
        ingredientRV = findViewById(R.id.addCosmetic_ingredientRV);

        ingredientRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ingredientItemRVAdapter = new IngredientItemRVAdapter();

        ingredientItemRVAdapter.setOnItemClickListener(new IngredientItemRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int pos) {
                final IngredientItemData ingredientItemData;

                ingredientItemData = ingredientItemRVAdapter.getItem(pos);

                // DB에 존재하지 않는 성분일 때만
                if(!ingredientItemData.getIsExist()) {
                    // 해당 성분 변경 Dialog 띄우기
                    AlertDialog.Builder builder;
                    AlertDialog alertDialog;
                    View dialogView;
                    TextView ingredientNameTV;
                    final EditText ingredientNameET;

                    builder = new AlertDialog.Builder(v.getContext());

                    dialogView = getLayoutInflater().inflate(R.layout.input_dialog, null);
                    ingredientNameTV = dialogView.findViewById(R.id.inputDialog_ingredientNameTV);
                    ingredientNameET = dialogView.findViewById(R.id.inputDialog_ingredientNameET);

                    ingredientNameTV.setText(ingredientItemData.getIngredientName());

                    builder.setView(dialogView);
                    builder.setTitle("성분 명 변경");

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ingredientItemRVAdapter.getItem(pos).setIngredientName(ingredientNameET.getText().toString());
                            ingredientItemRVAdapter.getItem(pos).setIsExist(true);

                            ingredientItemRVAdapter.notifyDataSetChanged();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        ingredientRV.setAdapter(ingredientItemRVAdapter);
    }

    // 추출한 성분 RecyclerView에 추가
    private void addIngredientsToRecyclerView() {
        // 먼저 초기화
        ingredientItemRVAdapter.delAllItem();

        // 성분 하나씩 뽑아서 추가
        for(String ingredient : ingredients) {
            final IngredientItemData ingredientItemData;

            ingredientItemData = new IngredientItemData();

            // '/' 가 포함되어있으면 '.'로 대체
            ingredient = ingredient.replaceAll("/", ".");

            ingredientItemData.setIngredientName(ingredient);

            // DB에 존재하는 성분인지 확인
            DatabaseManagement.getInstance().isExistIngredientFromDatabase(ingredient, new Callback<Boolean>() {
                @Override
                public void onCallback(Boolean data) {
                    // 존재하지 않는 성분
                    if(!data) {
                        ingredientItemData.setIngredientName(ingredientItemData.getIngredientName() + "(성분이 정확하지 않으니 클릭하여 수정해주세요.)");
                        ingredientItemData.setIsExist(false);
                    } else {
                        // 존재 하는 성분
                        ingredientItemData.setIsExist(true);
                    }

                    ingredientItemRVAdapter.addItem(ingredientItemData);

                    // 변경됬음을 표시
                    ingredientItemRVAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // 브랜드 명 및 제품 명 EditText 사용 가능하게
    private void setUsableBrandAndCosmeticName() {
        EditText brandNameET, cosmeticNameET;
        InputMethodManager imm;

        brandNameET = findViewById(R.id.addCosmetic_brandNameET);
        cosmeticNameET = findViewById(R.id.addCosmetic_cosmeticNameET);

        // 활성화
        brandNameET.setFocusableInTouchMode(true);
        brandNameET.setFocusable(true);

        cosmeticNameET.setFocusableInTouchMode(true);
        cosmeticNameET.setFocusable(true);

        // 브랜드 명에 포커스주기
        brandNameET.requestFocus();

        // 키보드가 화면 가리는 현상 방지
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(brandNameET, 0);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // 뒤로가기
            case R.id.addCosmetic_backIV:
                finish();

                break;

            // 완료
            case R.id.addCosmetic_completeBtn:
                EditText brandNameET, cosmeticNameET;

                // 제품 사진이 등록되었는지, 화장품 성분이 추가되었는지 확인
                if(cosmeticBitmap != null && ingredientItemRVAdapter.getItemCount() != 0) {
                    final String brandName, cosmeticName;

                    brandNameET = findViewById(R.id.addCosmetic_brandNameET);
                    cosmeticNameET = findViewById(R.id.addCosmetic_cosmeticNameET);

                    brandName = brandNameET.getText().toString();
                    cosmeticName = cosmeticNameET.getText().toString();

                    // 브랜드 명과 제품 명이 등록되었는지 확인
                    if(brandName != "" && cosmeticName != "" && brandName != "브랜드 명" && cosmeticName != "제품 명") {
                        // Cloud Storage에 화장품 이미지 등록
                        DatabaseManagement.getInstance().addCosmeticImageToDatabase(this, brandName, cosmeticName, cosmeticBitmap, new Callback<Boolean>() {
                            @Override
                            public void onCallback(Boolean data) {
                                // 등록 성공시
                                if(data) {
                                    CosmeticRankItemData cosmeticRankItemData;
                                    TextView cosmeticCategoryTV;

                                    cosmeticCategoryTV = findViewById(R.id.addCosmetic_cosmeticCategoryTV);

                                    cosmeticRankItemData = new CosmeticRankItemData(brandName, cosmeticName, cosmeticCategoryTV.getText().toString());

                                    // DB에 등록할 화장품에 추출한 성분들 등록하기
                                    for(int i = 0; i < ingredientItemRVAdapter.getItemCount(); i++) {
                                        IngredientItemData item;

                                        item = ingredientItemRVAdapter.getItem(i);

                                        cosmeticRankItemData.addIngredient(item);
                                    }

                                    // DB에 화장품 추가
                                    DatabaseManagement.getInstance().addCosmeticToDatabase(cosmeticRankItemData, new Callback<Boolean>() {
                                        @Override
                                        public void onCallback(Boolean data) {
                                            // 등록 성공시
                                            if(data != null) {
                                                // 메인 화면으로 가기
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }

                break;

            // 화장품 이미지 등록
            case R.id.addCosmetic_addCosmeticImageIV:
                // 카메라 띄우기
                showCamera(REQUEST_CAPTURE_IMAGE);

                break;

            // 성분 찍기
            case R.id.addCosmetic_captureIngredientBtn:
                // 카메라 띄우기
                showCamera(REQUEST_CAPTURE_INGREDENT);

                break;
        }
    }

    // 카메라 띄우기
    private void showCamera(int flag) {
        Intent intent;
        File photoFile;
        Uri photoUri;

        photoFile = null;

        try {
            photoFile = createImageFile();
        } catch(IOException e) {
            System.out.println("임시 이미지 파일 생성 실패: " + e.getMessage());

            return;
        }

        photoUri = FileProvider.getUriForFile(this, "com.example.dodam.fileprovider", photoFile);

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        startActivityForResult(intent, flag);
    }

    // 카메라 요청에 대한 응답
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 요청이 성공
        if(resultCode == RESULT_OK) {
            // 카메라 요청에 대한 응답일 때
            if (requestCode == REQUEST_CAPTURE_IMAGE) {
                ImageDecoder.Source source;
                ImageView addCosmeticIV;
                File file;

                file = new File(currentPhotoPath);

                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        cosmeticBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file));
                    } else {
                        source = ImageDecoder.createSource(this.getContentResolver(), Uri.fromFile(file));
                        cosmeticBitmap = ImageDecoder.decodeBitmap(source);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 브랜드 명과 제품 명을 직접 입력할 건지 선택 Dialog 띄우기
                showChoiceInputDialog();

                addCosmeticIV = findViewById(R.id.addCosmetic_addCosmeticImageIV);

                addCosmeticIV.setImageBitmap(cosmeticBitmap);
            } else if(requestCode == REQUEST_CAPTURE_INGREDENT || requestCode == REQUEST_CAPTURE_CROP) {
                File file;
                ImageDecoder.Source source;
                Bitmap ingredientBitmap;

                ingredientBitmap = null;

                file = new File(currentPhotoPath);

                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        ingredientBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file));
                    } else {
                        source = ImageDecoder.createSource(this.getContentResolver(), Uri.fromFile(file));
                        ingredientBitmap = ImageDecoder.decodeBitmap(source);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // CROP 일 때
                if(requestCode == REQUEST_CAPTURE_CROP) {
                    analysisBitmap(ingredientBitmap, REQUEST_CAPTURE_INGREDENT);
                } else {
                    // 성분 촬영 때
                    cropImage(file, ingredientBitmap);
                }
            }
        }
    }

    // Crop 요청
    private void cropImage(File file, Bitmap ingredientBitmap) {
        Intent intent;
        Uri photoUri;

        photoUri = FileProvider.getUriForFile(this, "com.example.dodam.fileprovider", file);

        intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(photoUri, "image/*");

        intent.putExtra("outputX", ingredientBitmap.getWidth());
        intent.putExtra("outputY", ingredientBitmap.getHeight());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        // Uri 접근에 대한 권한 승인
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        grantUriPermission("com.example.dodam.fileprovider", photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(intent, REQUEST_CAPTURE_CROP);
    }

    // 카메라로 촬영한 이미지를 파일로 저장
    private File createImageFile() throws IOException {
        String timeStamp;
        String imageFileName;
        File storageDir;
        File image;

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        imageFileName = "JPEG_" + timeStamp + "_";

        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    // 비트맵 분석
    private void analysisBitmap(Bitmap bitmap, final int requestCode) {
        FirebaseVisionImage visionImage;
        FirebaseVisionTextRecognizer detector;
        FirebaseVisionCloudTextRecognizerOptions options;

        options = new FirebaseVisionCloudTextRecognizerOptions.Builder().setLanguageHints(Arrays.asList("en", "ko")).build();

        detector = FirebaseVision.getInstance().getCloudTextRecognizer(options);

        visionImage = FirebaseVisionImage.fromBitmap(bitmap);

        detector.processImage(visionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                // 제품 촬영 때
                if(requestCode == REQUEST_CAPTURE_IMAGE) {
                    ArrayList<String> lineTexts;

                    lineTexts = new ArrayList<String>();

                    for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                        for (FirebaseVisionText.Line line : block.getLines()) {
                            String lineText = line.getText();

                            lineTexts.add(lineText);
                        }
                    }

                    // 브랜드 및 제품명 선택
                    selectBrandAndCosmeticName(lineTexts);
                } else if (requestCode == REQUEST_CAPTURE_INGREDENT) {
                    // 성분 명 촬영 때
                    for(FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                        String blockText;
                        String preText, postText;
                        int index, nextIndex;

                        blockText = block.getText();

                        // '전성분'이 안보이면 다음 블록으로
                        if(!blockText.contains("전성분")) {
                            continue;
                        }

                        System.out.println("원본");
                        System.out.println(blockText);

                        // 먼저 블록단위로 '('로 시작해서 ')'로 끝나는 부분 전부 제거
                        while((index = blockText.indexOf('(')) != -1) {
                            preText = blockText.substring(0, index);
                            index = blockText.indexOf(')');
                            postText = blockText.substring(index + 1);

                            blockText = preText + postText;
                        }

                        // 전성분 단어 빼내기
                        index = blockText.indexOf(('분'));

                        if(index != -1) {
                            blockText = blockText.substring(index + 2);
                        }

                        ingredients = new ArrayList<String>();

                        // ',' 단위로 자르는데 앞뒤가 숫자일 경우 자르지 말고 집어넣기
                        while((index = blockText.indexOf(',')) != -1) {
                            // 뒤가 숫자인지 확인
                            if(blockText.charAt(index + 1) >= '0' && blockText.charAt(index + 1) <= '9') {
                                // 다음 ',' 찾기
                                nextIndex = blockText.indexOf(',', index + 1);

                                // 한 성분 뗴어내기
                                preText = blockText.substring(0, nextIndex);

                                index = nextIndex;
                            } else {
                                // 한 성분 뗴어내기
                                preText = blockText.substring(0, index);
                            }

                            // 성분 목록에 추가
                            ingredients.add(preText);

                            // 시작위치 재설정
                            blockText = blockText.substring(index + 1);
                        }

                        // 마지막 항목 넣기
                        ingredients.add(blockText);

                        System.out.println("추출");

                        // 문자열 안에 줄넘김 및 공백 제거하기
                        for(int i = 0; i < ingredients.size(); i++) {
                            // 줄넘김 및 공백 제거
                            ingredients.set(i, ingredients.get(i).replaceAll("(\n| )", ""));
                            System.out.println(ingredients.get(i));
                        }

                        // RecyclerView에 추가
                        addIngredientsToRecyclerView();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("오류: " + e.getMessage());
            }
        });
    }

    // 화장품 카테고리 선택 Dialog 띄우기
    private void showChoiceCategoryDialog(final Callback<Boolean> callback) {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        final String[] categoryItems = {Constant.CATEGORY_SKINCARE, Constant.CATEGORY_CLEANSING, Constant.CATEGORY_MASKANDPACK
                , Constant.CATEGORY_SUNCARE, Constant.CATEGORY_BASE};
        final ArrayList<String> selectedItem;

        // 카테고리 먼저 선택
        builder = new AlertDialog.Builder(this);

        selectedItem = new ArrayList<String>();

        builder.setTitle("화장품 카테고리 선택");
        builder.setSingleChoiceItems(categoryItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem.clear();
                selectedItem.add(categoryItems[which]);
            }
        }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView cosmeticCategoryTV;

                cosmeticCategoryTV = findViewById(R.id.addCosmetic_cosmeticCategoryTV);
                cosmeticCategoryTV.setText(selectedItem.get(0));

                callback.onCallback(true);
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    // 브랜드 명과 제품 명을 인식으로 추출할 건지 직접 입력할 건지 Dialog 띄우기
    private void showChoiceInputDialog() {
        final AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        // 카테고리 선택 Dialog 띄우기
        showChoiceCategoryDialog(new Callback<Boolean>() {
            @Override
            public void onCallback(Boolean data) {
                // 정상적으로 처리됬을 때
                if(data) {
                    AlertDialog alertDialog;
                    String[] items = {"사진에서 추출", "직접 입력"};

                    builder.setTitle("브랜드 및 제품 명 입력");

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 사진에서 추출
                            if(which == 0) {
                                analysisBitmap(cosmeticBitmap, REQUEST_CAPTURE_IMAGE);
                            } else {
                                // EditText 입력 가능하게 하고 포커스 맞추기
                                setUsableBrandAndCosmeticName();
                            }
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    // 선택형 Dialog 띄우기
    private void showCheckBoxDialog(String title, final ArrayList<String> texts, final Callback<Boolean> callback) {
        final AlertDialog.Builder builder;
        final AlertDialog alertDialog;
        final ArrayList<String> selectedItems;
        final String[] items;

        items = texts.toArray(new String[texts.size()]);

        selectedItems = new ArrayList<String>();

        builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Checked 상태
                if(isChecked == true) {
                    selectedItems.add(items[which]);
                } else {
                    selectedItems.remove(items[which]);
                }
            }
        });

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedTexts = "";

                // Checked 된 항목들 붙이기
                for(String text : selectedItems) {
                    selectedTexts += text + " ";

                    // texts에 있는 문자열중에 선택된 항목들 제외시키기
                    texts.remove(text);
                }

                // 마지막 공백 제거
                selectedTexts = selectedTexts.substring(0, selectedTexts.length() - 1);

                // 브랜드 명 바꿔주기
                if(callback != null) {
                    EditText brandNameET;

                    brandNameET = findViewById(R.id.addCosmetic_brandNameET);
                    brandNameET.setText(selectedTexts);

                    callback.onCallback(false);
                } else {
                    // 제품 명 바꿔주기
                    EditText cosmeticNameET;

                    cosmeticNameET = findViewById(R.id.addCosmetic_cosmeticNameET);
                    cosmeticNameET.setText(selectedTexts);
                }
            }
        });

        alertDialog = builder.create();

        alertDialog.show();
    }

    // 브랜드 명 선택
    private void selectBrandAndCosmeticName(final ArrayList<String> texts) {
        showCheckBoxDialog("브랜드 명 선택", texts, new Callback<Boolean>() {
            @Override
            public void onCallback(Boolean data) {
                showCheckBoxDialog("제품 명 선택", texts, null);
            }
        });
    }
}