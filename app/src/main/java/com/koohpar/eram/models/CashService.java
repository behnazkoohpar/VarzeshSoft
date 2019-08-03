package com.koohpar.eram.models;

/**
 * Created by cmos on 03/03/2019.
 */

public class CashService {
    private static final long serialVersionUID = 1L;
    private String ID;
    private String ServiceType;
    private String Title;
    private String TotalAmount;
    private String MemberGradeDiscountPercent;
    private String DiscountPercent;
    private String DiscountAmount;
    private String TotalAmountAfterDiscount;
    private String TaxAndTollPercent;
    private String TaxAndTollAmount;
    private String PayableTotalAmount;

    public CashService(String id, String serviceType,  String title, String totalAmount,
                       String memberGradeDiscountPercent, String discountPercent, String discountAmount,
                       String totalAmountAfterDiscount, String taxAndTollPercent, String taxAndTollAmount,
                       String payableTotalAmount) {
        ID = id;
        ServiceType = serviceType;
        Title = title;
        TotalAmount = totalAmount;
        MemberGradeDiscountPercent = memberGradeDiscountPercent;
        DiscountPercent = discountPercent;
        DiscountAmount = discountAmount;
        TotalAmountAfterDiscount = totalAmountAfterDiscount;
        TaxAndTollPercent = taxAndTollPercent;
        TaxAndTollAmount = taxAndTollAmount;
        PayableTotalAmount = payableTotalAmount;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getMemberGradeDiscountPercent() {
        return MemberGradeDiscountPercent;
    }

    public void setMemberGradeDiscountPercent(String memberGradeDiscountPercent) {
        MemberGradeDiscountPercent = memberGradeDiscountPercent;
    }

    public String getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        DiscountPercent = discountPercent;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getTotalAmountAfterDiscount() {
        return TotalAmountAfterDiscount;
    }

    public void setTotalAmountAfterDiscount(String totalAmountAfterDiscount) {
        TotalAmountAfterDiscount = totalAmountAfterDiscount;
    }

    public String getTaxAndTollPercent() {
        return TaxAndTollPercent;
    }

    public void setTaxAndTollPercent(String taxAndTollPercent) {
        TaxAndTollPercent = taxAndTollPercent;
    }

    public String getTaxAndTollAmount() {
        return TaxAndTollAmount;
    }

    public void setTaxAndTollAmount(String taxAndTollAmount) {
        TaxAndTollAmount = taxAndTollAmount;
    }

    public String getPayableTotalAmount() {
        return PayableTotalAmount;
    }

    public void setPayableTotalAmount(String payableTotalAmount) {
        PayableTotalAmount = payableTotalAmount;
    }
}
