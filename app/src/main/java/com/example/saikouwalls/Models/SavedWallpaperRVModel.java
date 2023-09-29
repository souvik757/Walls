package com.example.saikouwalls.Models;

public class SavedWallpaperRVModel {
    private String ID ;
    private String URL ;

    public SavedWallpaperRVModel() {
    }

    public SavedWallpaperRVModel(String ID, String URL) {
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
