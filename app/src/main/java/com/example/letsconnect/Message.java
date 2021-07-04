package com.example.letsconnect;

public class Message {
    String message;
    String senderuid;
    long timestamp;
    String currentTime;

    public Message(String message, String senderuid, long timestamp, String currentTime) {
        this.message = message;
        this.senderuid = senderuid;
        this.timestamp = timestamp;
        this.currentTime = currentTime;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderuid() {
        return senderuid;
    }

    public void setSenderuid(String senderuid) {
        this.senderuid = senderuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
