package com.koohpar.eram.models;

/**
 * Created by cmos on 16/01/2018.
 */

public class Messages {

    String ID;
    String SendDate;
    String SendTime;
    String Title;
    String Seened;
    String MessageBody;

    public Messages(String id, String date, String time, String title, String seened, String message) {
        ID = id;
        SendTime = time;
        Title = title;
        MessageBody = message;
        SendDate = date;
        Seened = seened;
    }

    public String getSeened() {
        return Seened;
    }

    public void setSeened(String seened) {
        Seened = seened;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSendDate() {
        return SendDate;
    }

    public void setSendDate(String sendDate) {
        SendDate = sendDate;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessageBody() {
        return MessageBody;
    }

    public void setMessageBody(String messageBody) {
        MessageBody = messageBody;
    }
}
