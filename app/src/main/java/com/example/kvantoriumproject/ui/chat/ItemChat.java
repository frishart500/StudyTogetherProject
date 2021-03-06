package com.example.kvantoriumproject.ui.chat;

public class ItemChat {
    private String name, img, email, desc;
    String subject;
    public ItemChat(){}
    public ItemChat(String name, String img, String email,String desc, String subject){
        this.email = email;
        this.name = name;
        this.img = img;
        this.desc = desc;
        this.subject = subject;
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

