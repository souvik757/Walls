package com.example.saikouwalls.Models;

public class WallpaperRVModel {
    private String ID ;
    private String URL ;
    private String ALT ;
    private String PHOTOGRAPHER_NAME ;
    private String PHOTOGRAPHER_INFO_URL ;
    private String WIDTH ;
    private String HEIGHT ;

    public WallpaperRVModel() {
    }

    public WallpaperRVModel(String ID, String URL, String ALT, String PHOTOGRAPHER_NAME, String WIDTH, String HEIGHT , String PHOTOGRAPHER_INFO_URL) {
        this.ID = ID;
        this.URL = URL;
        this.ALT = ALT;
        this.PHOTOGRAPHER_NAME = PHOTOGRAPHER_NAME;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.PHOTOGRAPHER_INFO_URL = PHOTOGRAPHER_INFO_URL ;
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

    public String getALT() {
        return ALT;
    }

    public void setALT(String ALT) {
        this.ALT = ALT;
    }

    public String getPHOTOGRAPHER_NAME() {
        return PHOTOGRAPHER_NAME;
    }

    public void setPHOTOGRAPHER_NAME(String PHOTOGRAPHER_NAME) {
        this.PHOTOGRAPHER_NAME = PHOTOGRAPHER_NAME;
    }

    public String getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(String WIDTH) {
        this.WIDTH = WIDTH;
    }

    public String getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(String HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public String getPHOTOGRAPHER_INFO_URL() {
        return PHOTOGRAPHER_INFO_URL;
    }

    public void setPHOTOGRAPHER_INFO_URL(String PHOTOGRAPHER_INFO_URL) {
        this.PHOTOGRAPHER_INFO_URL = PHOTOGRAPHER_INFO_URL;
    }
}
