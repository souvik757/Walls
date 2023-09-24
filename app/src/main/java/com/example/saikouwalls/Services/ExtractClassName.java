package com.example.saikouwalls.Services;

public class ExtractClassName {
    public static String getName(String pkg){

        String pkgRev = String.valueOf(new StringBuilder(pkg).reverse()) ;
        StringBuilder str = new StringBuilder() ;
        for (char c : pkgRev.toCharArray()){
            if(c == '.')
                break ;
            else
                str.append(c) ;
        }
        return String.valueOf(str.reverse()) ;
    }
}
