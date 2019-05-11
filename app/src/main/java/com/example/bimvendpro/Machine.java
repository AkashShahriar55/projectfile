package com.example.bimvendpro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Machine implements Serializable {
    private String code;
    private String name;
    private String model;
    private String type;
    private String lastVisit;
    private String note;
    private int daysInService;
    private float totalCollected;
    private float vendsPerDay;
    private MachineInstall machineInstall;
    private List<MachineIngredients> machineIngredients;

    public Machine(String code, String name, String model, String type) {
        this.code = code;
        this.name = name;
        this.model = model;
        this.type = type;


    }

    public Machine(String code, String name, String model, String type, String lastVisit, String note, int daysInService, float totalCollected, float vendsPerDay, MachineInstall machineInstall) {
        this.code = code;
        this.name = name;
        this.model = model;
        this.type = type;
        this.machineInstall = machineInstall;
        this.lastVisit = lastVisit;
        this.note = note;
        this.daysInService = daysInService;
        this.totalCollected = totalCollected;
        this.vendsPerDay = vendsPerDay;

    }

    public Machine(String code, String name, String model, String type, String lastVisit, String note, int daysInService, float totalCollected, float vendsPerDay, MachineInstall machineInstall, List<MachineIngredients> machineIngredients) {
        this.code = code;
        this.name = name;
        this.model = model;
        this.type = type;
        this.machineInstall = machineInstall;
        this.lastVisit = lastVisit;
        this.note = note;
        this.daysInService = daysInService;
        this.totalCollected = totalCollected;
        this.vendsPerDay = vendsPerDay;
        this.machineIngredients = machineIngredients;
    }

    public Machine() {

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


    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public List<MachineIngredients> getMachineIngredients() {
        return machineIngredients;
    }

    public void setMachineIngredients(List<MachineIngredients> machineIngredients) {
        this.machineIngredients = machineIngredients;
    }
}
