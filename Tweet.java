package com.example.lonelytwitter;

import java.util.Date;

public abstract class Tweet implements Tweetable {
    private String message;
    private Date date;
    public Tweet(String message) {
        this.message = message;
        this.date = new Date();
    }
    public Tweet(String message, Date pushDate) {
        this.message = message;
        this.date = pushDate;
    }
    @Override
    public String getMessage() {
        return message;
    }
    @Override
    public Date getDate() {
        return date;
    }
    public void setMessage(String msg) {
        this.message = msg;
    }
    public void getDate(Date dt) {
        this.date = dt;
    }
    public abstract Boolean isImportant();
}
