package com.example.letsconnect;

public class FirebaseModel {

    String name;
    String image;
    String userUid;
    String status;

    public FirebaseModel(String name, String image, String userUid, String status) {
        this.name = name;
        this.image = image;
        this.userUid = userUid;
        this.status = status;
    }

    public FirebaseModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
