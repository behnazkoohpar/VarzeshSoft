package com.koohpar.eram.models;

import java.io.Serializable;

/**
 * Created by cmos on 11/30/2017.
 */

public class Factor implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ReceptionUnit_Text;
    private String LockerNumber;
    private String TotalPrice;
    private String InvoiceDate;
    private String FactorNo;
    private String FactorID;

    public Factor(String receptionUnit_Text, String lockerNumber, String totalPrice,String invoiceDate,String factorNo,String factorID) {
        ReceptionUnit_Text = receptionUnit_Text;
        LockerNumber = lockerNumber;
        TotalPrice = totalPrice;
        InvoiceDate = invoiceDate;
        FactorNo = factorNo;
        FactorID = factorID;

    }

    public String getFactorID() {
        return FactorID;
    }

    public void setFactorID(String factorID) {
        FactorID = factorID;
    }

    public String getReceptionUnit_Text() {
        return ReceptionUnit_Text;
    }

    public void setReceptionUnit_Text(String receptionUnit_Text) {
        ReceptionUnit_Text = receptionUnit_Text;
    }

    public String getLockerNumber() {
        return LockerNumber;
    }

    public void setLockerNumber(String lockerNumber) {
        LockerNumber = lockerNumber;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getFactorNo() {
        return FactorNo;
    }

    public void setFactorNo(String factorNo) {
        FactorNo = factorNo;
    }
}
