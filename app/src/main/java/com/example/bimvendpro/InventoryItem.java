package com.example.bimvendpro;

public class InventoryItem {
    private String code;
    private String productName;
    private String productType;
    private int inStock;
    private int inWarehouse;
    private int inMachine;
    private int unitPerCase;
    private float lastCost;
    //private InventoryItemHistory inventoryItemHistory;


    public InventoryItem(String code, String productName, String productType, int inStock, int inWarehouse, int inMachine, int unitPerCase) {
        this.code = code;
        this.productName = productName;
        this.productType = productType;
        this.inStock = inStock;
        this.inWarehouse = inWarehouse;
        this.inMachine = inMachine;
        this.unitPerCase = unitPerCase;
        this.lastCost = 0;
    }

    public InventoryItem() {

    }

    public InventoryItem(String code, String productName, String productType, int inStock, int inWarehouse, int inMachine, int unitPerCase, float lastCost) {
        this.code = code;
        this.productName = productName;
        this.productType = productType;
        this.inStock = inStock;
        this.inWarehouse = inWarehouse;
        this.inMachine = inMachine;
        this.unitPerCase = unitPerCase;
        this.lastCost = lastCost;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getInWarehouse() {
        return inWarehouse;
    }

    public void setInWarehouse(int inWarehouse) {
        this.inWarehouse = inWarehouse;
    }

    public int getInMachine() {
        return inMachine;
    }

    public void setInMachine(int inMachine) {
        this.inMachine = inMachine;
    }

    public int getUnitPerCase() {
        return unitPerCase;
    }

    public void setUnitPerCase(int unitPerCase) {
        this.unitPerCase = unitPerCase;
    }

    public float getLastCost() {
        return lastCost;
    }

    public void setLastCost(float lastCost) {
        this.lastCost = lastCost;
    }

/*
    public InventoryItemHistory getInventoryItemHistory() {
        return inventoryItemHistory;
    }

    public void setInventoryItemHistory(InventoryItemHistory inventoryItemHistory) {
        this.inventoryItemHistory = inventoryItemHistory;
    }
    */
}
