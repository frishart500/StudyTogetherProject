package com.example.kvantoriumproject;

public class FinishTask {
    private String id1, id2, idOfTask, UID1, UID2, uidForCompare;

    public FinishTask(String id1, String id2, String idOfTask, String UID1, String UID2) {
        this.id1 = id1;
        this.id2 = id2;
        this.idOfTask = idOfTask;
        this.UID1 = UID1;
        this.UID2 = UID2;
    }

    public String getUidForCompare() {
        return uidForCompare;
    }

    public void setUidForCompare(String uidForCompare) {
        this.uidForCompare = uidForCompare;
    }

    public String getIdOfTask() {
        return idOfTask;
    }

    public void setIdOfTask(String idOfTask) {
        this.idOfTask = idOfTask;
    }

    public String getUID1() {
        return UID1;
    }

    public void setUID1(String UID1) {
        this.UID1 = UID1;
    }

    public String getUID2() {
        return UID2;
    }

    public void setUID2(String UID2) {
        this.UID2 = UID2;
    }

    public FinishTask(){}

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }
}
