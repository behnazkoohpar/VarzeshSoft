package com.koohpar.eram.models;

import java.io.Serializable;

/**
 * Created by cmos on 12/1/2017.
 */

public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ID;
    private String CreationTime;
    private String MembershipFile;
    private String Amount;
    private String ReceiveType;

    public Transaction(String id, String creationTime, String membershipFile, String amount, String receiveType) {
        ID = id;
        CreationTime = creationTime;
        MembershipFile = membershipFile;
        Amount = amount;
        ReceiveType = receiveType;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        CreationTime = creationTime;
    }

    public String getMembershipFile() {
        return MembershipFile;
    }

    public void setMembershipFile(String membershipFile) {
        MembershipFile = membershipFile;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getReceiveType() {
        return ReceiveType;
    }

    public void setReceiveType(String receiveType) {
        ReceiveType = receiveType;
    }
}