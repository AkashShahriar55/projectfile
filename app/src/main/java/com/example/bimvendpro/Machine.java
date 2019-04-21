package com.example.bimvendpro;

import java.util.Date;

public class Machine {
    private String code;
    private String name;
    private String model;
    private String type;
    private String location;
    private Date installDate;
    private Date lastVisit;
    private int daysInService;
    private float totalCollected;
    private float vendsPerDay;
    private MachineInstall machineInstall;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public int getDaysInService() {
        return daysInService;
    }

    public void setDaysInService(int daysInService) {
        this.daysInService = daysInService;
    }

    public float getTotalCollected() {
        return totalCollected;
    }

    public void setTotalCollected(float totalCollected) {
        this.totalCollected = totalCollected;
    }

    public float getVendsPerDay() {
        return vendsPerDay;
    }

    public void setVendsPerDay(float vendsPerDay) {
        this.vendsPerDay = vendsPerDay;
    }

    public Date getInstallDate() {
        return installDate;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public MachineInstall getMachineInstall() {
        return machineInstall;
    }

    public void setMachineInstall(MachineInstall machineInstall) {
        this.machineInstall = machineInstall;
    }
}
