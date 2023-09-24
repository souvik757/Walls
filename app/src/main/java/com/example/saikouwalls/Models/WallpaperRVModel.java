package com.example.saikouwalls.Models;

public class WallpaperRVModel {
    private String ID ;
    private String URL ;

    public WallpaperRVModel() {
    }

    public WallpaperRVModel(String ID, String URL) {
        this.ID = ID;
        this.URL = URL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
