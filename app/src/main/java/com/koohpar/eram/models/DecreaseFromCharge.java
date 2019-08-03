package com.koohpar.eram.models;

public class DecreaseFromCharge {
    private static final long serialVersionUID = 1L;
    private String FromTime;
    private String ToTime;
    private String InputAmount;
    private String DayNo;
    private String FromExtraTime;
    private String ExtraTimeAmount;

    public DecreaseFromCharge(String fromTime, String toTime, String inputAmount, String dayNo,
                              String fromExtraTime, String extraTimeAmount) {
        FromTime = fromTime;
        ToTime = toTime;
        InputAmount = inputAmount;
        DayNo = dayNo;
        FromExtraTime = fromExtraTime;
        ExtraTimeAmount = extraTimeAmount;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public String getInputAmount() {
        return InputAmount;
    }

    public void setInputAmount(String inputAmount) {
        InputAmount = inputAmount;
    }

    public String getDayNo() {
        return DayNo;
    }

    public void setDayNo(String dayNo) {
        DayNo = dayNo;
    }

    public String getFromExtraTime() {
        return FromExtraTime;
    }

    public void setFromExtraTime(String fromExtraTime) {
        FromExtraTime = fromExtraTime;
    }

    public String getExtraTimeAmount() {
        return ExtraTimeAmount;
    }

    public void setExtraTimeAmount(String extraTimeAmount) {
        ExtraTimeAmount = extraTimeAmount;
    }
}
