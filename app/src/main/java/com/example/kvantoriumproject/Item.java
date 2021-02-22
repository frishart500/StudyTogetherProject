package com.example.kvantoriumproject;

public class Item {
    private String name;
    private String imgUri;
    private String subject;
    private String points;
    private String describe;
    private String email;

    public Item(){}
    public Item(String name, String imgUri, String subject, String points, String describe, String email){
        this.points = points;
        this.describe = describe;
        this.name = name;
        this.imgUri = imgUri;
        this.subject = subject;
        this.email = email;
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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
