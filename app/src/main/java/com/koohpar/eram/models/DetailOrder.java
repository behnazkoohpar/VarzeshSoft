package com.koohpar.eram.models;

import java.io.Serializable;

/**
 * Created by cmos on 11/27/2017.
 */

public class DetailOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    private String ReceptionDate;
    private String UsedSessionsCount;
    private String RemainedSessionsCount;
    private String OranizationName;

    public DetailOrder(String receptionDate, String usedSessionsCount, String remainedSessionsCount, String oranizationName) {
        ReceptionDate = receptionDate;
        UsedSessionsCount = usedSessionsCount;
        RemainedSessionsCount = remainedSessionsCount;
        OranizationName = oranizationName;
    }

    public String getReceptionDate() {
        return ReceptionDate;
    }

    public void setReceptionDate(String receptionDate) {
        ReceptionDate = receptionDate;
    }

    public String getUsedSessionsCount() {
        return UsedSessionsCount;
    }

    public void setUsedSessionsCount(String usedSessionsCount) {
        UsedSessionsCount = usedSessionsCount;
    }

    public String getRemainedSessionsCount() {
        return RemainedSessionsCount;
    }

    public void setRemainedSessionsCount(String remainedSessionsCount) {
        RemainedSessionsCount = remainedSessionsCount;
    }

    public String getOranizationName() {
        return OranizationName;
    }

    public void setOranizationName(String oranizationName) {
        OranizationName = oranizationName;
    }
}
