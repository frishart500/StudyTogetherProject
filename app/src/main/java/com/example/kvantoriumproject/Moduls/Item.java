package com.example.kvantoriumproject.Moduls;

public class Item {
    private String name;
    private String imgUri;
    private String subject;
    private String points;
    private String describe;
    private String email;
    private String phone;
    private String nameOfTask;
    private String dateToFinish;
    private String classText;
    private String id, idOfTask;
    private String subjectOfUser, describeOfUser;
    private String imgUri1, imgUri2;

    public Item(){}
    public Item(String name, String imgUri, String subject, String points, String describe, String email,
                String phone, String nameOfTask, String dateToFinish, String classText, String subjectOfUser, String describeOfUser, String id, String idOfTask, String imgUri1){
        this.points = points;
        this.describe = describe;
        this.name = name;
        this.imgUri = imgUri;
        this.subject = subject;
        this.email = email;
        this.phone = phone;
        this.nameOfTask = nameOfTask;
        this.dateToFinish = dateToFinish;
        this.classText = classText;
        this.subjectOfUser = subjectOfUser;
        this.describeOfUser = describeOfUser;
        this.id = id;
        this.idOfTask = idOfTask;
        this.imgUri1 = imgUri1;
    }

    public String getImgUri1() {
        return imgUri1;
    }

    public void setImgUri1(String imgUri1) {
        this.imgUri1 = imgUri1;
    }

    public String getImgUri2() {
        return imgUri2;
    }

    public void setImgUri2(String imgUri2) {
        this.imgUri2 = imgUri2;
    }

    public String getIdOfTask() {
        return idOfTask;
    }

    public void setIdOfTask(String idOfTask) {
        this.idOfTask = idOfTask;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectOfUser() {
        return subjectOfUser;
    }

    public void setSubjectOfUser(String subjectOfUser) {
        this.subjectOfUser = subjectOfUser;
    }

    public String getDescribeOfUser() {
        return describeOfUser;
    }

    public void setDescribeOfUser(String describeOfUser) {
        this.describeOfUser = describeOfUser;
    }

    public String getClassText() {
        return classText;
    }

    public void setClassText(String classText) {
        this.classText = classText;
    }

    public String getNameOfTask() {
        return nameOfTask;
    }

    public void setNameOfTask(String nameOfTask) {
        this.nameOfTask = nameOfTask;
    }

    public String getDateToFinish() {
        return dateToFinish;
    }

    public void setDateToFinish(String dateToFinish) {
        this.dateToFinish = dateToFinish;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
