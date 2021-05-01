package com.example.kvantoriumproject.ui.chats;

public class ItemChat {
    private String name, img, email, desc, classText, nameOfTask, nameAnotherPerson;
    private String subject, phone, price, describe, dateToFinish, myEmail, userId, anotherId, justId, id, imgUri;
    public ItemChat(){}
    public ItemChat(String name, String img, String email,String desc, String subject, String classText, String nameOfTask, String nameAnotherPerson,
                    String phone, String price, String describe, String dateToFinish, String myEmail, String userId, String anotherId, String justId, String id, String imgUri){
        this.email = email;
        this.myEmail = myEmail;
        this.phone = phone;
        this.price = price;
        this.describe = describe;
        this.dateToFinish = dateToFinish;
        this.name = name;
        this.img = img;
        this.desc = desc;
        this.nameAnotherPerson = nameAnotherPerson;
        this.subject = subject;
        this.classText = classText;
        this.nameOfTask = nameOfTask;
        this.userId = userId;
        this.anotherId = anotherId;
        this.justId = justId;
        this.imgUri = imgUri;
        this.id = id;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJustId() {
        return justId;
    }

    public void setJustId(String justId) {
        this.justId = justId;
    }

    public String getAnotherId() {
        return anotherId;
    }

    public void setAnotherId(String anotherId) {
        this.anotherId = anotherId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDateToFinish() {
        return dateToFinish;
    }

    public void setDateToFinish(String dateToFinish) {
        this.dateToFinish = dateToFinish;
    }

    public String getNameAnotherPerson() {
        return nameAnotherPerson;
    }

    public void setNameAnotherPerson(String nameAnotherPerson) {
        this.nameAnotherPerson = nameAnotherPerson;
    }

    public String getNameOfTask() {
        return nameOfTask;
    }

    public void setNameOfTask(String nameOfTask) {
        this.nameOfTask = nameOfTask;
    }

    public String getClassText() {
        return classText;
    }

    public void setClassText(String classText) {
        this.classText = classText;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

