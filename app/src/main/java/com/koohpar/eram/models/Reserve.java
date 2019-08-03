package com.koohpar.eram.models;

public class Reserve {
    private static final long serialVersionUID = 1L;
    private String ID;
    private String Title;
    private String Code;

    public Reserve(String id, String title, String code ) {
        ID = id;
        Title = title;
        Code = code;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
