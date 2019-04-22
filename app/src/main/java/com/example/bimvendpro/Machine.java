package com.example.bimvendpro;

import java.util.Date;

public class Machine {
    private String code;
    private String name;
    private String model;
    private String type;
    private Date lastVisit;
    private int daysInService;
    private float totalCollected;
    private float vendsPerDay;
    private MachineInstall machineInstall;

    public Machine(String code, String name, String model, String type) {
        this.code = code;
        this.name = name;
        this.model = model;
        this.type = type;
        lastVisit=null;
        machineInstall=new MachineInstall();
    }

    public Machine(){
        code="not set";
        name="not set";
        model="not set";
        type="not set";
        lastVisit=new Date();
        machineInstall=new MachineInstall();
    }

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



    public MachineInstall getMachineInstall() {
        return machineInstall;
    }

    public void setMachineInstall(MachineInstall machineInstall) {
        this.machineInstall = machineInstall;
    }
}
