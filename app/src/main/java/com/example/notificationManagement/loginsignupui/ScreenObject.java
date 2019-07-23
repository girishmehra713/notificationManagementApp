package com.example.notificationManagement.loginsignupui;

public class ScreenObject {
    private String Title,Description;
    private  int Img,Back;

    public ScreenObject(String title, String description, int img,int back) {
        Title = title;
        Description = description;
        Img = img;
        Back = back;

    }

    public ScreenObject(int back) {
        Back = back;
    }

    public int getBack() {
        return Back;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getImg() {
        return Img;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImg(int img) {
        Img = img;
    }
}
