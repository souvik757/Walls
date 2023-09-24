package com.example.saikouwalls.Models;

public class CategoryRVModel {
    private String ID ;
    private String category ;
    private String imgUrl ;

    public CategoryRVModel(String ID , String category, String imgUrl) {
        this.ID = ID ;
        this.category = category;
        this.imgUrl = imgUrl;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
