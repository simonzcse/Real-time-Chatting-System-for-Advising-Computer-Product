package com.example.fypchatapps.Model;

public class Chat {
    private String dateTime;
    private String receiver;
    private String sender;
    private String text;
    private boolean isseen;

    public Chat(String dateTime, String receiver, String sender, String text, boolean isseen) {
        this.dateTime = dateTime;
        this.receiver = receiver;
        this.sender = sender;
        this.text = text;
        this.isseen = isseen;
    }
    public Chat(){}

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public boolean isIsseen() {
        return isseen;
    }
}

