package com.example.dodam.database;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dodam.data.Constant;
import com.example.dodam.data.CosmeticRankItemData;
import com.example.dodam.data.DataManagement;
import com.example.dodam.data.IngredientItem;
import com.example.dodam.data.UserData;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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

                        callback.onCallback(documentSnapshot.toObject(UserData.class));
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
    public void signUpEmail(final Activity activity, final UserData user) {
        // 유저 생성 작업
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        // 작업 완료시
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // 성공적으로 가입되었을 때
                            if(task.isSuccessful()) {
                                // DB에 유저 정보 등록
                                addUserToDatabase(user);

                                activity.finish();
                            } else {
                                // 가입 실패시
                                System.out.println("이메일 회원가입 실패");

                                Toast.makeText(activity, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }

    // DB에 사용자 등록
    public void addUserToDatabase(UserData user) {
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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // 실패시
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("유저 데이터 DB 등록 실패");
                    }
                });
    }

    // DB에 등록된 성분인지 확인
    public void isExistIngredientFromDatabase(String ingredientName, final Callback<Boolean> callback) {
        DocumentReference ingredientRef;

        ingredientRef = database.collection(Constant.DB_COLLECTION_INGREDIENTS).document(ingredientName);
        ingredientRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // 존재하므로 true
                        if(documentSnapshot.getData() != null) {
                            callback.onCallback(true);
                        } else {
                            // 존재하지 않으므로 false
                            callback.onCallback(false);
                        }
                    }
                });
    }

    // Cloud Storage에 이미지 파일 등록
    public void addCosmeticImageToDatabase(String brandName, String cosmeticName, Bitmap cosmeticBitmap, final Callback<Uri> callback) {
        final StorageReference brandRef;
        ByteArrayOutputStream baos;
        byte[] data;
        UploadTask uploadTask;
        Task<Uri> urlTask;

        // 브랜드 명 폴더에 화장품 명 이름으로 저장
        brandRef = FirebaseStorage.getInstance().getReference().child(Constant.DB_COLLECTION_BRANDS).child(brandName).child(cosmeticName + ".jpg");

        baos = new ByteArrayOutputStream();

        // JPEG로 변경하여 baos에 넣기
        cosmeticBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        data = baos.toByteArray();

        // 업로드할 data 담기
        uploadTask = brandRef.putBytes(data);

        urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                // 실패시 예외 발생
                if(!task.isSuccessful()) {
                    throw task.getException();
                }

                // 다운로드 Url 반환
                return brandRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                // 업로드 성공시
                if(task.isSuccessful()) {
                    callback.onCallback(task.getResult());
                } else {
                    // 실패시
                    callback.onCallback(null);
                }
            }
        });
    }

    // DB에 화장품 등록
    public void addCosmeticToDatabase(final CosmeticRankItemData cosmeticRankItemData, final Callback<Boolean> callback) {
        final CollectionReference brandRef;

        brandRef = database.collection(Constant.DB_COLLECTION_BRANDS).document(cosmeticRankItemData.getBrandName())
                .collection(cosmeticRankItemData.getCosmeticName());

        brandRef.add(cosmeticRankItemData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                cosmeticRankItemData.setCosmeticId(documentReference.getId());

                // 다시 업데이트
                documentReference.update("cosmeticId", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference userRef;

                        // 유저정보에도 업데이트
                        userRef = database.collection(Constant.DB_COLLECTION_USERS).document(DataManagement.getInstance().getUserData().getEmail());
                        userRef.update(Constant.DB_FIELD_REGISTERCOSMETICS, documentReference.getId());

                        callback.onCallback(true);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(false);
            }
        });
    }

/*
    화장품에 들어가는 성분 DB 등록용
    public void addIngredientToDatabase(IngredientItem ingredientItem) {
        CollectionReference ingredientRef;

        ingredientRef = database.collection(Constant.DB_COLLECTION_INGREDIENTS);
        ingredientRef.document(ingredientItem.getName_ko())
                .set(ingredientItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("디비 실패");
                    }
                });
    }
 */
}
