package com.example.saikouwalls.Services;

public class ExtractPhotoURLNumber {
    public static String getNumber(String url){
        return url.substring(33 , 40) ;
    }
}
