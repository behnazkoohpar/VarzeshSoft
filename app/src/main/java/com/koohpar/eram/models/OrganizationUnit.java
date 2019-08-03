package com.koohpar.eram.models;

/**
 * Created by cmos on 03/03/2019.
 */

public class OrganizationUnit {

    private static final long serialVersionUID = 1L;
    private String ID;
    private String Name;

    public OrganizationUnit(String id, String name) {
        ID=id;
        Name=name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
