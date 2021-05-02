package com.example.kvantoriumproject.Chat;

public class AwesomeMessage {
    private String text;
    private String name;
    private String imgUrl;
    private String sender;
    private String recipient, id, idOfTask;
    private boolean isMine;

    public AwesomeMessage() {
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

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
