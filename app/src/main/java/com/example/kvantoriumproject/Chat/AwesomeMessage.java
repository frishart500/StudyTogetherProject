package com.example.kvantoriumproject.Chat;

public class AwesomeMessage {
    private String text;
    private String name;
    private String imgUrl;
    private String sender;
    private String recipient;

    public AwesomeMessage(){}

    public AwesomeMessage(String text, String name, String imgUrl, String sender, String recipient) {
        this.text = text;
        this.name = name;
        this.imgUrl = imgUrl;
        this.sender = sender;
        this.recipient = recipient;
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
