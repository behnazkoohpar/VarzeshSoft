package com.koohpar.eram.models;

/**
 * Created by cmos on 02/02/2018.
 */

public class DetailFactor {

    String CreationTime;
    String Code;
    String Stuff_Text;
    String Value;
    String UnitPrice;
    String TotalPrice;

    public DetailFactor(String creationTime, String code, String stuff_Text, String value, String unitPrice, String totalPrice) {
        CreationTime = creationTime;
        Code = code;
        Stuff_Text = stuff_Text;
        Value = value;
        UnitPrice = unitPrice;
        TotalPrice = totalPrice;
    }

    public String getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        CreationTime = creationTime;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getStuff_Text() {
        return Stuff_Text;
    }

    public void setStuff_Text(String stuff_Text) {
        Stuff_Text = stuff_Text;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }
}
