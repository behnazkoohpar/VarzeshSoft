package com.koohpar.eram.models;

import java.io.Serializable;

/**
 * Created by cmos on 11/28/2017.
 */

public class DetailCreditOrder implements Serializable {


    private static final long serialVersionUID = 1L;
    String CreationTime;
    String LockerNumber;
    String EnterTime;
    String ExitTime;
    String Description;
    String ElapsedMinutes;
    String InputAmount;
    String TimeAmount;
    String SalesInvoiceAmount;
    String SubServiceAmount;
    String UsedCreditChargeTotalAmount;
    String CreditTotalAmount;
    String ChargeAmountNote;
    String OrganizationName;

    public DetailCreditOrder(String creationTime,
                                  String lockerNumbe,
                                  String enterTime,
                                  String exitTime,
                                  String description,
                                  String elapsedMinutes,
                                  String inputAmount,
                                  String timeAmount,
                                  String salesInvoiceAmount,
                                  String subServiceAmount,
                                  String usedCreditChargeTotalAmount,
                                  String creditTotalAmount,
                                  String chargeAmountNote,
                                  String organizationName) {

        CreationTime = creationTime;
        LockerNumber = lockerNumbe;
        EnterTime = enterTime;
        ExitTime = exitTime;
        Description = description;
        ElapsedMinutes = elapsedMinutes;
        InputAmount = inputAmount;
        TimeAmount = timeAmount;
        SalesInvoiceAmount = salesInvoiceAmount;
        SubServiceAmount = subServiceAmount;
        UsedCreditChargeTotalAmount = usedCreditChargeTotalAmount;
        CreditTotalAmount = creditTotalAmount;
        ChargeAmountNote = chargeAmountNote;
        OrganizationName = organizationName;
    }

    public String getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        CreationTime = creationTime;
    }

    public String getLockerNumber() {
        return LockerNumber;
    }

    public void setLockerNumber(String lockerNumber) {
        LockerNumber = lockerNumber;
    }

    public String getEnterTime() {
        return EnterTime;
    }

    public void setEnterTime(String enterTime) {
        EnterTime = enterTime;
    }

    public String getExitTime() {
        return ExitTime;
    }

    public void setExitTime(String exitTime) {
        ExitTime = exitTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getElapsedMinutes() {
        return ElapsedMinutes;
    }

    public void setElapsedMinutes(String elapsedMinutes) {
        ElapsedMinutes = elapsedMinutes;
    }

    public String getInputAmount() {
        return InputAmount;
    }

    public void setInputAmount(String inputAmount) {
        InputAmount = inputAmount;
    }

    public String getTimeAmount() {
        return TimeAmount;
    }

    public void setTimeAmount(String timeAmount) {
        TimeAmount = timeAmount;
    }

    public String getSalesInvoiceAmount() {
        return SalesInvoiceAmount;
    }

    public void setSalesInvoiceAmount(String salesInvoiceAmount) {
        SalesInvoiceAmount = salesInvoiceAmount;
    }

    public String getSubServiceAmount() {
        return SubServiceAmount;
    }

    public void setSubServiceAmount(String subServiceAmount) {
        SubServiceAmount = subServiceAmount;
    }

    public String getUsedCreditChargeTotalAmount() {
        return UsedCreditChargeTotalAmount;
    }

    public void setUsedCreditChargeTotalAmount(String usedCreditChargeTotalAmount) {
        UsedCreditChargeTotalAmount = usedCreditChargeTotalAmount;
    }

    public String getCreditTotalAmount() {
        return CreditTotalAmount;
    }

    public void setCreditTotalAmount(String creditTotalAmount) {
        CreditTotalAmount = creditTotalAmount;
    }

    public String getChargeAmountNote() {
        return ChargeAmountNote;
    }

    public void setChargeAmountNote(String chargeAmountNote) {
        ChargeAmountNote = chargeAmountNote;
    }

    public String getOrganizationName() {
        return OrganizationName;
    }

    public void setOrganizationName(String OrganizationName) {
        OrganizationName = OrganizationName;
    }
}
