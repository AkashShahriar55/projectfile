package com.example.bimvendpro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DriverItem implements Serializable {
    private String name;
    private String address;
    private String sidNo;
    private String idNo;
    private String workingHour;
    private String password;
    private List<DriverImages> driverImagesList;

    public DriverItem() {
    }

    public DriverItem(String name, String address, String sidNo, String idNo, String workingHour,String password, List<DriverImages> driverImagesList) {
        this.name = name;
        this.address = address;
        this.sidNo = sidNo;
        this.idNo = idNo;
        this.workingHour = workingHour;
        this.password = password;
        this.driverImagesList = driverImagesList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSidNo() {
        return sidNo;
    }

    public void setSidNo(String sidNo) {
        this.sidNo = sidNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(String workingHour) {
        this.workingHour = workingHour;
    }

    public List<DriverImages> getDriverImagesList() {
        return driverImagesList;
    }

    public void setDriverImagesList(List<DriverImages> driverImagesList) {
        this.driverImagesList = driverImagesList;
    }
}
