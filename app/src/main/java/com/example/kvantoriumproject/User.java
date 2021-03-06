package com.example.kvantoriumproject;

public class User {

    private String name, email, data, describtion, phone, subject, imgUri, points, status;

    public User(){}
    public User(String email, String name, String data, String describtion, String phone, String subject, String imgUri, String points){
        this.email = email;
        this.name = name;
        this.data = data;
        this.describtion = describtion;
        this.imgUri = imgUri;
        this.phone = phone;
        this.subject = subject;
        this.points = points;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getData() {
        return data;
    }

    public String getPoints() {
        return points;
    }

    public String getImgUri() {
        return imgUri;
    }

    public String getDescribtion() {
        return describtion;
    }

    public String getSubject() {
        return subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
