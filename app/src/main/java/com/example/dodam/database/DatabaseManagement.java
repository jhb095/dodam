package com.example.dodam.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dodam.R;
import com.example.dodam.data.BrandCosmeticItems;
import com.example.dodam.data.BrandItemData;
import com.example.dodam.data.Constant;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.data.CosmeticReviewItems;
import com.example.dodam.data.DataManagement;
import com.example.dodam.data.IngredientDocument;
import com.example.dodam.data.IngredientItem;
import com.example.dodam.data.IngredientItemData;
import com.example.dodam.data.ReviewItemData;
import com.example.dodam.data.UserData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// 데이터베이스 전반적인 것을 다루는 클래스(싱글톤)
public class DatabaseManagement {
    private static DatabaseManagement dbM = new DatabaseManagement();
    private FirebaseAuth firebaseAuth;   // 파이어베이스 인증 객체
    private FirebaseFirestore database;  // 데이터베이스

    // 생성자
    private DatabaseManagement() {
        firebaseAuth = FirebaseAuth.getInstance();
        database     = FirebaseFirestore.getInstance();
    }

    // 객체 가져오기
    public static DatabaseManagement getInstance() {
        return dbM;
    }

    // 로그인 이메일 확인
    public void signInEmail(final Activity activity, final String email, String password, final Callback<Boolean> callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    // 작업 완료시
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 이메일 로그인 성공
                        if(task.isSuccessful()) {
                            // DB에서 유저데이터 가져오기
                            getUserDataFromDatabase(email, new Callback<UserData>() {
                                @Override
                                public void onCallback(UserData data) {
                                    // 유저데이터를 성공적으로 가져왔을 때
                                    if(data != null) {
                                        DataManagement.getInstance().setUserData(data);

                                        callback.onCallback(true);
                                    } else {
                                        callback.onCallback(false);
                                    }
                                }
                            });
                        } else {
                            // 로그인 실패시
                            System.out.println("이메일 로그인 실패");

                            Toast.makeText(activity, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // DB에서 유저 정보 가져오기(콜백으로 처리)
    private void getUserDataFromDatabase(String email, final Callback<UserData> callback) {
        DocumentReference userRef;

        userRef = database.collection(Constant.DB_COLLECTION_USERS).document(email);
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserData user;

                        user = documentSnapshot.toObject(UserData.class);

                        callback.onCallback(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("DB에서 유저 정보를 가져오지 못함");
                    }
                });
    }

    // 이메일 회원가입
    public void signUpEmail(final UserData user, final Callback<Boolean> callback) {
        // 유저 생성 작업
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        // 작업 완료시
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // 성공적으로 가입되었을 때
                            if(task.isSuccessful()) {
                                // DB에 유저 정보 등록
                                addUserToDatabase(user, callback);
                            } else {
                                // 가입 실패시
                                callback.onCallback(false);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(false);
            }
        });
    }

    // DB에 사용자 등록
    public void addUserToDatabase(UserData user, final Callback<Boolean> callback) {
        CollectionReference userRef;

        // user에 id값이 없으면 넣어줘야 한다.
        if(user.getId() == null) {
            user.setId(firebaseAuth.getCurrentUser().getUid());
        }

        // DB Collection에 해당 유저 Document 추가
        userRef = database.collection(Constant.DB_COLLECTION_USERS);
        userRef.document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // 실패시
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false);
                    }
                });
    }

    // DB에 사용자 피부타입 업데이트
    public void updateUserSkinTypeToDatabase(UserData user, final Callback<Boolean> callback) {
        CollectionReference userRef;

        // DB Collection에 해당 유저 Document 추가
        userRef = database.collection(Constant.DB_COLLECTION_USERS);
        userRef.document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // 실패시
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false);
                    }
                });
    }

    // DB에 화장품 리뷰(좋아요) 업데이트
    public void updateCosmeticReviewLikeToDatabase(final ReviewItemData review, final String cosmeticId, final Callback<Boolean> callback) {
        // 업데이트 하고자 하는 화장퓸 리뷰 목록 가져오기
        getCosmeticReviewsFromDatabase(cosmeticId, new Callback<List<ReviewItemData>>() {
            @Override
            public void onCallback(List<ReviewItemData> data) {
                DocumentReference reviewRef;
                CosmeticReviewItems cosmeticReviewItems;

                cosmeticReviewItems = new CosmeticReviewItems();

                if(data != null) {
                    // 리뷰 목록에서 현재 리뷰에 해당하는 것을 지우고 좋아요 또는 싫어요를 고친 것을 집어넣기
                    for(ReviewItemData item : data) {
                        if(item.getUserId().equals(review.getUserId())) {
                            data.remove(item);

                            break;
                        }
                    }

                    cosmeticReviewItems.setReviews(data);
                }

                cosmeticReviewItems.getReviews().add(review);

                reviewRef = database.collection(Constant.DB_COLLECTION_REVIEWS).document(cosmeticId);
                reviewRef.set(cosmeticReviewItems)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    callback.onCallback(true);
                                } else {
                                    callback.onCallback(false);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false);
                    }
                });
            }
        });
    }

    // DB에 등록된 성분인지 확인
    public void isExistIngredientFromDatabase(final String ingredientName, final Callback<Boolean> callback) {
        CollectionReference ingredientRef;

        ingredientRef = database.collection(Constant.DB_COLLECTION_INGREDIENTS);
        ingredientRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        IngredientDocument ingredientDocument;
                        List<IngredientItem> ingredientItems;

                        ingredientDocument = document.toObject(IngredientDocument.class);

                        ingredientItems = ingredientDocument.getIngredientItems();

                        for(IngredientItem ingredientItem : ingredientItems) {
                            if(ingredientItem.getName_ko().equals(ingredientName) || ingredientItem.getName_en().equals(ingredientName)) {
                                callback.onCallback(true);

                                return;
                            }
                        }
                    }

                    callback.onCallback(false);
                } else {
                    callback.onCallback(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(false);
            }
        });
    }

    // Cloud Storage에 이미지 파일 등록
    public void addCosmeticImageToDatabase(Context context, String brandName, String cosmeticName, Bitmap cosmeticBitmap, final Callback<Boolean> callback) {
        final StorageReference brandRef;
        ByteArrayOutputStream baos;
        byte[] data;
        UploadTask uploadTask;
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;
        View dialogView;
        final TextView progressTV;
        LayoutInflater inflater;

        builder = new AlertDialog.Builder(context);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Progress
        dialogView = inflater.inflate(R.layout.progress_dialog, null);
        progressTV = dialogView.findViewById(R.id.progressDialog_progressTV);

        builder.setView(dialogView);
        builder.setTitle("업로드 중");

        alertDialog = builder.create();
        alertDialog.show();

        // 공백 제거후 소문자로
        brandName = brandName.replace(" ", "");
        brandName = brandName.toLowerCase();

        // 브랜드 명 폴더에 화장품 명 이름으로 저장
        brandRef = FirebaseStorage.getInstance().getReference().child(Constant.DB_COLLECTION_BRANDS).child(brandName).child(cosmeticName + ".jpg");

        baos = new ByteArrayOutputStream();

        // JPEG로 변경하여 baos에 넣기
        cosmeticBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        data = baos.toByteArray();

        // 업로드할 data 담기
        uploadTask = brandRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                alertDialog.dismiss();

                callback.onCallback(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.dismiss();

                callback.onCallback(false);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress;

                progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                progressTV.setText(((int)progress) + "% 완료");
            }
        });
    }

    // DB에 화장품 등록
    public void addCosmeticToDatabase(final CosmeticRankItemData cosmeticRankItemData, final Callback<Boolean> callback) {
        getBrandCosmeticsFromDatabase(cosmeticRankItemData.getBrandName(), new Callback<List<CosmeticRankItemData>>() {
            @Override
            public void onCallback(List<CosmeticRankItemData> data) {
                DocumentReference brandRef;
                BrandCosmeticItems brandCosmeticItems;
                final UserData userData;

                brandCosmeticItems = new BrandCosmeticItems();

                if(data != null) {
                    brandCosmeticItems.setCosmetics(data);
                }

                userData = DataManagement.getInstance().getUserData();

                // 화장품 Id는 브랜드 명 + "_" + 제품 명 + "_" + 유저Id
                cosmeticRankItemData.setCosmeticId(cosmeticRankItemData.getBrandName() + "_" + cosmeticRankItemData.getCosmeticName() + "_" + userData.getId());

                brandCosmeticItems.getCosmetics().add(cosmeticRankItemData);

                brandRef = database.collection(Constant.DB_COLLECTION_BRANDS)
                        .document(cosmeticRankItemData.getBrandName());

                brandRef.set(brandCosmeticItems)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // 작업 성공시
                                if(task.isSuccessful()) {
                                    DocumentReference userRef;
                                    DocumentReference cosmeticRef;

                                    userData.getRegisterCosmetics().add(cosmeticRankItemData.getCosmeticId());

                                    // 유저 데이터 업데이트
                                    userRef = database.collection(Constant.DB_COLLECTION_USERS).document(userData.getEmail());
                                    userRef.update(Constant.DB_FIELD_REGISTERCOSMETICS, userData.getRegisterCosmetics());

                                    // DB 화장품 경로에도 추가
                                    cosmeticRef = database.collection(Constant.DB_COLLECTION_COSMETICS).document(cosmeticRankItemData.getCosmeticId());
                                    cosmeticRef.set(cosmeticRankItemData);

                                    callback.onCallback(true);
                                } else {
                                    callback.onCallback(false);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false);
                    }
                });
            }
        });
    }

    // DB로부터 전체 화장품 목록 가져오기
    public void getCosmeticsFromDatabase(final Callback<List<CosmeticRankItemData>> callback) {
        CollectionReference cosmeticRef;

        cosmeticRef = database.collection(Constant.DB_COLLECTION_COSMETICS);
        cosmeticRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<CosmeticRankItemData> cosmetics;

                            cosmetics = new ArrayList<>();

                            // 한 항목씩 뽑아서 추가
                            for(QueryDocumentSnapshot cosmetic : task.getResult()) {
                                cosmetics.add(cosmetic.toObject(CosmeticRankItemData.class));
                            }

                            callback.onCallback(cosmetics);
                        } else {
                            callback.onCallback(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(null);
            }
        });
    }

    // DB로부터 브랜드 목록 가져오기
    public void getBrandsFromDatabase(final Callback<ArrayList<BrandItemData>> callback) {
        CollectionReference brandRef;

        brandRef = database.collection(Constant.DB_COLLECTION_BRANDS);
        brandRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // 작업 성공시
                        if(task.isSuccessful()) {
                            ArrayList<BrandItemData> brandItems;

                            brandItems = new ArrayList<>();

                            // 한 항목씩 뽑아서 추가
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                BrandItemData brandItem;

                                brandItem = new BrandItemData();
                                brandItem.setBrandName(document.getId());

                                brandItems.add(brandItem);
                            }

                            callback.onCallback(brandItems);
                        } else {
                            callback.onCallback(null);
                        }
                    }
                });
    }

    // 해당 브랜드 제품 목록 가져오기
    public void getBrandCosmeticsFromDatabase(String brandName, final Callback<List<CosmeticRankItemData>> callback) {
        DocumentReference brandRef;

        brandRef = database.collection(Constant.DB_COLLECTION_BRANDS).document(brandName);
        brandRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document;
                            List<CosmeticRankItemData> brandCosmeticItems;

                            document = task.getResult();

                            if(document != null && document.exists()) {
                                brandCosmeticItems = document.toObject(BrandCosmeticItems.class).getCosmetics();

                                callback.onCallback(brandCosmeticItems);
                            } else {
                                callback.onCallback(null);
                            }
                        }
                    }
                });
    }

    // 화장품 제품의 이미지 가져오기
    public void getCosmeticImageFromStorage(String brandName, String cosmeticName, final Callback<Uri> callback) {
        FirebaseStorage storage;
        StorageReference storageRef;

        // 공백 제거후 소문자로
        brandName = brandName.replace(" ", "");
        brandName = brandName.toLowerCase();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://dodam-a1823.appspot.com")
                .child(Constant.DB_COLLECTION_BRANDS)
                .child(brandName)
                .child(cosmeticName + ".jpg");

        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    Uri uri = task.getResult();

                    callback.onCallback(uri);
                } else {
                    callback.onCallback(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(null);
            }
        });
    }

    // 브랜드 이미지 가져오기
    public void getBrandImageFromStorage(String brandName, final Callback<Uri> callback) {
        FirebaseStorage storage;
        StorageReference storageRef;

        // 공백 제거 후 소문자로
        brandName = brandName.replace(" ", "");
        brandName = brandName.toLowerCase();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://dodam-a1823.appspot.com")
                .child(Constant.DB_COLLECTION_BRANDS)
                .child(brandName)
                .child(Constant.DB_COLLECTION_LOGO_BRAND);

        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    Uri uri = task.getResult();

                    callback.onCallback(uri);
                } else {
                    callback.onCallback(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(null);
            }
        });
    }

    // DB에 리뷰 추가
    public void addCosmeticReviewToDatabase(final ReviewItemData reviewItemData, final String userEmail, final CosmeticRankItemData cosmeticItem, final Callback<Boolean> callback) {
        getCosmeticReviewsFromDatabase(cosmeticItem.getCosmeticId(), new Callback<List<ReviewItemData>>() {
            @Override
            public void onCallback(List<ReviewItemData> data) {
                DocumentReference reviewRef;
                DocumentReference cosmeticRef;
                CosmeticReviewItems cosmeticReviewItems;
                int[] skinType1Count, skinType2Count;
                int[] ageCount;
                int popularAge, popularSkinType1, popularSkinType2;
                int ageIndex, skinType1Index, skinType2Index;

                skinType1Count = new int[4];
                skinType2Count = new int[4];
                ageCount = new int[5];

                cosmeticReviewItems = new CosmeticReviewItems();

                if(data != null) {
                    cosmeticReviewItems.setReviews(data);
                }

                cosmeticReviewItems.getReviews().add(reviewItemData);

                // 해당 화장품을 가장 많이 사용한 피부타입과 연령대 찾기
                for(ReviewItemData review : cosmeticReviewItems.getReviews()) {
                    String[] splitedString;

                    splitedString = review.getUserInfo().split("/");

                    // 연령대
                    switch(splitedString[0]) {
                        case "10대 미만":
                            ageCount[0]++;
                            break;

                        case "10대":
                            ageCount[1]++;
                            break;

                        case "20대":
                            ageCount[2]++;
                            break;

                        case "30대":
                            ageCount[3]++;
                            break;

                        case "40대 이상":
                            ageCount[4]++;
                            break;
                    }

                    // 피부타입1
                    switch(splitedString[1]) {
                        case Constant.SKIN_DRY:
                            skinType1Count[Constant.SKIN_DRY_INT]++;

                            break;

                        case Constant.SKIN_WEAK_DRY:
                            skinType1Count[Constant.SKIN_WEAK_DRY_INT]++;

                            break;

                        case Constant.SKIN_WEAK_OILY:
                            skinType1Count[Constant.SKIN_WEAK_OILY_INT]++;

                            break;

                        case Constant.SKIN_OILY:
                            skinType1Count[Constant.SKIN_OILY_INT]++;

                            break;
                    }

                    // 피부타입2
                    switch(splitedString[2]) {
                        case Constant.SKIN_SENSITIVE:
                            skinType2Count[Constant.SKIN_SENSITIVE_INT]++;

                            break;

                        case Constant.SKIN_WEAK_SENSITIVE:
                            skinType2Count[Constant.SKIN_WEAK_SENSITIVE_INT]++;

                            break;

                        case Constant.SKIN_WEAK_RESISTANT:
                            skinType2Count[Constant.SKIN_WEAK_RESISTANT_INT]++;

                            break;

                        case Constant.SKIN_RESISTANT:
                            skinType2Count[Constant.SKIN_RESISTANT_INT]++;

                            break;
                    }
                }

                popularAge = ageCount[0];
                ageIndex = 0;

                for(int i = 1; i < ageCount.length; i++) {
                    if(popularAge < ageCount[i]) {
                        popularAge = ageCount[i];
                        ageIndex = i;
                    }
                }

                popularSkinType1 = skinType1Count[0];
                skinType1Index = 0;

                for(int i = 1; i < skinType1Count.length; i++) {
                    if(popularSkinType1 < skinType1Count[i]) {
                        popularSkinType1 = skinType1Count[i];
                        skinType1Index = i;
                    }
                }

                popularSkinType2 = skinType2Count[0];
                skinType2Index = 0;

                for(int i = 1; i < skinType2Count.length; i++) {
                    if(popularSkinType2 < skinType2Count[i]) {
                        popularSkinType2 = skinType2Count[i];
                        skinType2Index = i;
                    }
                }

                switch(ageIndex) {
                    case 0:
                        cosmeticItem.setPopularAge("10대 미만");
                        break;

                    case 1:
                        cosmeticItem.setPopularAge("10대");
                        break;

                    case 2:
                        cosmeticItem.setPopularAge("20대");
                        break;

                    case 3:
                        cosmeticItem.setPopularAge("30대");
                        break;

                    case 4:
                        cosmeticItem.setPopularAge("40대 이상");
                        break;
                }

                switch(skinType1Index) {
                    case Constant.SKIN_DRY_INT:
                        cosmeticItem.setPopularSkinType1(Constant.SKIN_DRY);

                        break;

                    case Constant.SKIN_WEAK_DRY_INT:
                        cosmeticItem.setPopularSkinType1(Constant.SKIN_WEAK_DRY);

                        break;

                    case Constant.SKIN_WEAK_OILY_INT:
                        cosmeticItem.setPopularSkinType1(Constant.SKIN_WEAK_OILY);

                        break;

                    case Constant.SKIN_OILY_INT:
                        cosmeticItem.setPopularSkinType1(Constant.SKIN_OILY);
                }

                switch(skinType2Index) {
                    case Constant.SKIN_SENSITIVE_INT:
                        cosmeticItem.setPopularSkinType2(Constant.SKIN_SENSITIVE);

                        break;

                    case Constant.SKIN_WEAK_SENSITIVE_INT:
                        cosmeticItem.setPopularSkinType2(Constant.SKIN_WEAK_SENSITIVE);

                        break;

                    case Constant.SKIN_WEAK_RESISTANT_INT:
                        cosmeticItem.setPopularSkinType2(Constant.SKIN_WEAK_RESISTANT);

                        break;

                    case Constant.SKIN_RESISTANT_INT:
                        cosmeticItem.setPopularSkinType2(Constant.SKIN_RESISTANT);
                }

                cosmeticRef = database.collection(Constant.DB_COLLECTION_COSMETICS).document(cosmeticItem.getCosmeticId());
                cosmeticRef.set(cosmeticItem);

                reviewRef = database.collection(Constant.DB_COLLECTION_REVIEWS).document(cosmeticItem.getCosmeticId());
                reviewRef.set(cosmeticReviewItems)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                DocumentReference userRef;
                                UserData userData;

                                userData = DataManagement.getInstance().getUserData();

                                userData.getRegisterReviews().add(cosmeticItem.getCosmeticId());

                                // 유저 데이터 업데이트
                                userRef = database.collection(Constant.DB_COLLECTION_USERS).document(userEmail);
                                userRef.update(Constant.DB_FIELD_REGISTERREVIEWS, userData.getRegisterReviews());

                                callback.onCallback(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false);
                    }
                });
            }
        });
    }

    // 해당 제품 리뷰 가져오기
    public void getCosmeticReviewsFromDatabase(String cosmeticId, final Callback<List<ReviewItemData>> callback) {
        DocumentReference reviewRef;

        reviewRef = database.collection(Constant.DB_COLLECTION_REVIEWS).document(cosmeticId);
        reviewRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document;
                            List<ReviewItemData> cosmeticReviewItems;

                            document = task.getResult();

                            if(document != null && document.exists()) {
                                cosmeticReviewItems = document.toObject(CosmeticReviewItems.class).getReviews();

                                callback.onCallback(cosmeticReviewItems);
                            } else {
                                callback.onCallback(null);
                            }
                        } else {
                            callback.onCallback(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(null);
            }
        });
    }

    // 화장품 평점 업데이트
    public void updateCosmeticRate(final float rate, final CosmeticRankItemData cosmeticRankItemData, final Callback<Boolean> callback) {
        // 업데이트 하고자 하는 제품 브랜드의 모든 화장품 목록 가져오기
        getBrandCosmeticsFromDatabase(cosmeticRankItemData.getBrandName(), new Callback<List<CosmeticRankItemData>>() {
            @Override
            public void onCallback(List<CosmeticRankItemData> data) {
                DocumentReference brandRef;
                DocumentReference cosmeticRef;
                BrandCosmeticItems brandCosmeticItems;

                brandCosmeticItems = new BrandCosmeticItems();

                if(data != null) {
                    // 제품 목록에서 현재 화장품에 해당하는 것을 지우고 평점을 고친 것을 집어넣기
                    for(CosmeticRankItemData item : data) {
                        if(item.getCosmeticId().equals(cosmeticRankItemData.getCosmeticId())) {
                            data.remove(item);

                            break;
                        }
                    }

                    brandCosmeticItems.setCosmetics(data);
                }

                cosmeticRankItemData.setRate((cosmeticRankItemData.getRate() * cosmeticRankItemData.getReviewCount() + rate) / (cosmeticRankItemData.getReviewCount() + 1));
                cosmeticRankItemData.setReviewCount(cosmeticRankItemData.getReviewCount() + 1);

                brandCosmeticItems.getCosmetics().add(cosmeticRankItemData);

                brandRef = database.collection(Constant.DB_COLLECTION_BRANDS).document(cosmeticRankItemData.getBrandName());
                brandRef.set(brandCosmeticItems)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                callback.onCallback(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false);
                    }
                });

                // 해당 제품 경로DB에도 갱신해야함
                // DB 화장품 경로에도 추가
                cosmeticRef = database.collection(Constant.DB_COLLECTION_COSMETICS).document(cosmeticRankItemData.getCosmeticId());
                cosmeticRef.set(cosmeticRankItemData);
            }
        });
    }

    // 해당 유저와 안맞는 성분을 제외한 화장품 목록 가져오기
    public void getUserCorrectCosmeticFromDatabase(final Callback<List<CosmeticRankItemData>> callback) {
        CollectionReference cosmeticRef;

        cosmeticRef = database.collection(Constant.DB_COLLECTION_COSMETICS);
        cosmeticRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<CosmeticRankItemData> cosmetics;
                            List<String> incorrectIngredientNames;
                            UserData userData;

                            userData = DataManagement.getInstance().getUserData();
                            incorrectIngredientNames = userData.getIncorrectIngredients();

                            cosmetics = new ArrayList<>();

                            for(QueryDocumentSnapshot document : task.getResult()) {
                                CosmeticRankItemData cosmetic;
                                int flag = 0;

                                cosmetic = document.toObject(CosmeticRankItemData.class);

                                // 유저의 안맞는 성분과 비교
                                for(IngredientItemData cosmeticIngredient : cosmetic.getIngredients()) {
                                    // 안맞는 성분이 포함되어있다면 제외
                                    if(incorrectIngredientNames.contains(cosmeticIngredient.getIngredientName())) {
                                        flag = 1;
                                        break;
                                    }
                                }

                                if(flag == 1) {
                                    continue;
                                }

                                cosmetics.add(cosmetic);
                            }

                            callback.onCallback(cosmetics);
                        } else {
                            callback.onCallback(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(null);
            }
        });
    }

    // 해당 유저가 올린 화장품 목록 가져오기(flag가 0이면 전체, 1이면 안맞는 화장품)
    public void getUserCosmeticFromDatabase(final int flag, final Callback<List<CosmeticRankItemData>> callback) {
        CollectionReference cosmeticRef;

        cosmeticRef = database.collection(Constant.DB_COLLECTION_COSMETICS);
        cosmeticRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            UserData userData;
                            List<String> userCosmeticId;
                            List<CosmeticRankItemData> cosmetics;

                            cosmetics = new ArrayList<>();

                            userData = DataManagement.getInstance().getUserData();
                            userCosmeticId = null;

                            if(flag == 0) {
                                userCosmeticId = userData.getRegisterCosmetics();
                            } else if(flag == 1) {
                                userCosmeticId = userData.getIncorrectCosmetics();
                            }

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CosmeticRankItemData data;

                                data = document.toObject(CosmeticRankItemData.class);

                                for (String cosmeticId : userCosmeticId) {
                                    if (cosmeticId.equals(data.getCosmeticId())) {
                                        cosmetics.add(data);

                                        break;
                                    }
                                }
                            }

                            callback.onCallback(cosmetics);
                        } else {
                            callback.onCallback(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(null);
            }
        });
    }


    // 화장품에 들어가는 성분 DB 등록용
    public void addIngredientToDatabase(IngredientDocument ingredientItem, int number) {
        DocumentReference ingredientRef;

        ingredientRef = database.collection(Constant.DB_COLLECTION_INGREDIENTS).document(String.valueOf(number));
        ingredientRef.set(ingredientItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("디비 실패");
                        e.printStackTrace();
                    }
                });
    }
}