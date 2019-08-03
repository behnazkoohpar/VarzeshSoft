package com.koohpar.eram.models;

/**
 * Created by cmos on 11/27/2017.
 */

public class Order {

    private static final long serialVersionUID = 1L;
    private String ID;
    private String ServiceID;
    private String ServiceTitle;
    private String ServiceType_Text;
    private String RegistrationSerial;
    private String ExpireDate;
    private String RegisterationUsedSessionsCount;
    private String RegisterationRemainedSessionsCount;
    private String CreditChargeUsedAmount;
    private String CreditChargeRemainedAmount;
    private String MembershipFileID;
    private String ServiceTotalAmount;

    public Order( String ID,String serviceID,String membershipFileID ,String serviceTitle, String serviceType_Text, String registrationSerial
            , String expireDate, String registerationUsedSessionsCount, String registerationRemainedSessionsCount, String creditChargeUsedAmount,
                 String creditChargeRemainedAmount, String serviceTotalAmount) {
        this.ID = ID;
        ServiceID = serviceID;
        ServiceTitle = serviceTitle;
        MembershipFileID = membershipFileID;
        ServiceType_Text = serviceType_Text;
        RegistrationSerial = registrationSerial;
        ExpireDate = expireDate;
        RegisterationUsedSessionsCount = registerationUsedSessionsCount;
        RegisterationRemainedSessionsCount = registerationRemainedSessionsCount;
        CreditChargeUsedAmount = creditChargeUsedAmount;
        CreditChargeRemainedAmount = creditChargeRemainedAmount;
        ServiceTotalAmount = serviceTotalAmount;
    }

    public String getMembershipFileID() {
        return MembershipFileID;
    }

    public void setMembershipFileID(String membershipFileID) {
        MembershipFileID = membershipFileID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getServiceID() {
        return ServiceID;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }

    public String getServiceTitle() {
        return ServiceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        ServiceTitle = serviceTitle;
    }

    public String getServiceType_Text() {
        return ServiceType_Text;
    }

    public void setServiceType_Text(String serviceType_Text) {
        ServiceType_Text = serviceType_Text;
    }

    public String getRegistrationSerial() {
        return RegistrationSerial;
    }

    public void setRegistrationSerial(String registrationSerial) {
        RegistrationSerial = registrationSerial;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public String getRegisterationUsedSessionsCount() {
        return RegisterationUsedSessionsCount;
    }

    public void setRegisterationUsedSessionsCount(String registerationUsedSessionsCount) {
        RegisterationUsedSessionsCount = registerationUsedSessionsCount;
    }

    public String getRegisterationRemainedSessionsCount() {
        return RegisterationRemainedSessionsCount;
    }

    public void setRegisterationRemainedSessionsCount(String registerationRemainedSessionsCount) {
        RegisterationRemainedSessionsCount = registerationRemainedSessionsCount;
    }

    public String getCreditChargeUsedAmount() {
        return CreditChargeUsedAmount;
    }

    public void setCreditChargeUsedAmount(String creditChargeUsedAmount) {
        CreditChargeUsedAmount = creditChargeUsedAmount;
    }

    public String getCreditChargeRemainedAmount() {
        return CreditChargeRemainedAmount;
    }

    public void setCreditChargeRemainedAmount(String creditChargeRemainedAmount) {
        CreditChargeRemainedAmount = creditChargeRemainedAmount;
    }

    public String getServiceTotalAmount() {
        return ServiceTotalAmount;
    }

    public void setServiceTotalAmount(String serviceTotalAmount) {
        ServiceTotalAmount = serviceTotalAmount;
    }
}
