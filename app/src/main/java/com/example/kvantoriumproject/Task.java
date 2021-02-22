package com.example.kvantoriumproject;

public class Task {
    private String subject, describe;

    public Task(String subjectForTask, String describe) {
        this.describe = describe;
        this.subject = subjectForTask;
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
