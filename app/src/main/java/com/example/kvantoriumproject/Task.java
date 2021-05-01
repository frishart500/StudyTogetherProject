package com.example.kvantoriumproject;

public class Task {
    private String subject, describe, name, email, points, img, phone, nameOfTask, dateToFinish, classText, subjectOfUser, describtionOfUser, idOfUser, idOfTask, imgUri1;

    public Task(String subjectForTask, String describe) {
        this.describe = describe;
        this.subject = subjectForTask;
    }

    public Task(){}

    public String getImgUri1() {
        return imgUri1;
    }

    public void setImgUri1(String imgUri1) {
        this.imgUri1 = imgUri1;
    }

    public String getIdOfTask() {
        return idOfTask;
    }

    public void setIdOfTask(String idOfTask) {
        this.idOfTask = idOfTask;
    }

    public String getId() {
        return idOfUser;
    }

    public void setId(String idOfUser) {
        this.idOfUser = idOfUser;
    }

    public String getSubjectOfUser() {
        return subjectOfUser;
    }

    public void setSubjectOfUser(String subjectOfUser) {
        this.subjectOfUser = subjectOfUser;
    }

    public String getDescribtionOfUser() {
        return describtionOfUser;
    }

    public void setDescribtionOfUser(String describtionOfUser) {
        this.describtionOfUser = describtionOfUser;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
