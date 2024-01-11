package com.serenespotssqlite.ass;

public class User {
    private int id;
    private String imagePath;
    private String name;
    private String admissionNo;
    private String text;

    public User() {

    }

    public User(int id, String imagePath, String name, String admissionNo, String text) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.admissionNo = admissionNo;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
