package com.example.studytogetherproject.Moduls;

public class Users {

    private String name, email, data, describtion, phone, subject, imgUri, points;
    private String countOfHowMuchTasksCreated, id, average, howMuchNotifications, howMuchTasksDone, gender, status;

    public Users(){}
    public Users(String email, String name, String data, String describtion, String phone, String subject, String imgUri, String points, String countOfHowMuchTasksCreated, String average, String howMuchNotifications, String howMuchTasksDone, String gender, String status){
        this.email = email;
        this.name = name;
        this.data = data;
        this.describtion = describtion;
        this.howMuchNotifications = howMuchNotifications;
        this.imgUri = imgUri;
        this.phone = phone;
        this.subject = subject;
        this.points = points;
        this.countOfHowMuchTasksCreated = countOfHowMuchTasksCreated;
        this.average = average;
        this.gender = gender;
        this.status = status;
        this.howMuchTasksDone = howMuchTasksDone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHowMuchTasksDone() {
        return howMuchTasksDone;
    }

    public void setHowMuchTasksDone(String howMuchTasksDone) {
        this.howMuchTasksDone = howMuchTasksDone;
    }

    public String getHowMuchNotifications() {
        return howMuchNotifications;
    }

    public void setHowMuchNotifications(String howMuchNotifications) {
        this.howMuchNotifications = howMuchNotifications;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountOfHowMuchTasksCreated() {
        return countOfHowMuchTasksCreated;
    }

    public void setCountOfHowMuchTasksCreated(String countOfHowMuchTasksCreated) {
        this.countOfHowMuchTasksCreated = countOfHowMuchTasksCreated;
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
