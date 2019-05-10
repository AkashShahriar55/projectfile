package com.example.bimvendpro;

import java.io.Serializable;
import java.util.List;

public class TripMachines implements Serializable {
    private String name;
    private String location;
    private String type;
    private double cashCollected;
    private int coins;
    private int bills;
    private String adjust;
    private String comment;
    private List<TripMachineProduct> tripMachineProducts;

    public TripMachines(String name, String location, String type, List<TripMachineProduct> tripMachineProducts) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.cashCollected = 0;
        this.comment = "";
        this.tripMachineProducts = tripMachineProducts;
        this.coins = 0;
        this.bills = 0;
        this.adjust = "0";
    }



    public TripMachines() {
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getBills() {
        return bills;
    }

    public void setBills(int bills) {
        this.bills = bills;
    }

    public String getAdjust() {
        return adjust;
    }

    public void setAdjust(String adjust) {
        this.adjust = adjust;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCashCollected() {
        return cashCollected;
    }

    public void setCashCollected(double cashCollected) {
        this.cashCollected = cashCollected;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<TripMachineProduct> getTripMachineProducts() {
        return tripMachineProducts;
    }

    public void setTripMachineProducts(List<TripMachineProduct> tripMachineProducts) {
        this.tripMachineProducts = tripMachineProducts;
    }
}
