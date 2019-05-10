package com.example.bimvendpro;

import java.io.Serializable;

public class TripMachineProduct implements Serializable {
    private String productName;
    private int lastCount;
    private int presentCount;
    private int soldOrWin;
    private int filled;
    private int newCount;

    public TripMachineProduct(String productName, int lastCount) {
        this.productName = productName;
        this.lastCount = lastCount;
        this.presentCount = 0;
        this.soldOrWin = 0;
        this.filled = 0;
        this.newCount = 0;
    }

    public TripMachineProduct() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getLastCount() {
        return lastCount;
    }

    public void setLastCount(int lastCount) {
        this.lastCount = lastCount;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public int getSoldOrWin() {
        return soldOrWin;
    }

    public void setSoldOrWin(int soldOrWin) {
        this.soldOrWin = soldOrWin;
    }

    public int getFilled() {
        return filled;
    }

    public void setFilled(int filled) {
        this.filled = filled;
    }

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }
}
