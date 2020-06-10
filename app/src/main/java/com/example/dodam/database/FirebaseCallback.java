package com.example.dodam.database;

public interface FirebaseCallback<T> {
    void onCallback(T data);
}
