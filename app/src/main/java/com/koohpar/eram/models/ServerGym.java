package com.koohpar.eram.models;

import java.io.Serializable;

/**
 * Created by cmos on 15/10/2018.
 */

public class ServerGym implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ID;
    private String ServerAddress;
    private String Name;
    private String PhoneNumber;
    private String SupporterName;
    private String SupporterPhone;
    private String Address;
    private String Email;
    private String GenderType;
    private String CreateDateTime;
    private String CreateShamsiDate;
    private String ExpireDate;
    private String ExpireShamsiDate;


    public ServerGym(String ID, String serverAddress, String name, String phoneNumber, String supporterName, String supporterPhone, String address, String email, String genderType, String createDateTime, String createShamsiDate, String expireDate, String expireShamsiDate) {
        this.ID = ID;
        ServerAddress = serverAddress;
        Name = name;
        PhoneNumber = phoneNumber;
        SupporterName = supporterName;
        SupporterPhone = supporterPhone;
        Address = address;
        Email = email;
        GenderType = genderType;
        CreateDateTime = createDateTime;
        CreateShamsiDate = createShamsiDate;
        ExpireDate = expireDate;
        ExpireShamsiDate = expireShamsiDate;
    }

    public ServerGym() {


    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getServerAddress() {
        return ServerAddress;
    }

    public void setServerAddress(String serverAddress) {
        ServerAddress = serverAddress;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getSupporterName() {
        return SupporterName;
    }

    public void setSupporterName(String supporterName) {
        SupporterName = supporterName;
    }

    public String getSupporterPhone() {
        return SupporterPhone;
    }

    public void setSupporterPhone(String supporterPhone) {
        SupporterPhone = supporterPhone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGenderType() {
        return GenderType;
    }

    public void setGenderType(String genderType) {
        GenderType = genderType;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        CreateDateTime = createDateTime;
    }

    public String getCreateShamsiDate() {
        return CreateShamsiDate;
    }

    public void setCreateShamsiDate(String createShamsiDate) {
        CreateShamsiDate = createShamsiDate;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public String getExpireShamsiDate() {
        return ExpireShamsiDate;
    }

    public void setExpireShamsiDate(String expireShamsiDate) {
        ExpireShamsiDate = expireShamsiDate;
    }
}
