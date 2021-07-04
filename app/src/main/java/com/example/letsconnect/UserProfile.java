package com.example.letsconnect;

public class UserProfile {

    public String userName,userUid;

    public UserProfile() {
    }

    public UserProfile(String userName, String userUid) {
        this.userName = userName;
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
