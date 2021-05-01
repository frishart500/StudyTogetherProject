package com.example.kvantoriumproject;

public class ChooseInf {
    private String comment;
    private String raiting;
    private String email;
    private String myEmail;
    private String name;
    private String ratingMiddle;

    public String getRatingMiddle() {
        return ratingMiddle;
    }

    public void setRatingMiddle(String ratingMiddle) {
        this.ratingMiddle = ratingMiddle;
    }

    public ChooseInf(String name, String comment, String raiting){
        this.name = name;
        this.comment = comment;
        this.raiting = raiting;
    }


    public ChooseInf(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    public String getComment() {
        return comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRaiting() {
        return raiting;
    }

    public void setRaiting(String raiting) {
        this.raiting = raiting;
    }
}
