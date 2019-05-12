package com.example.bimvendpro;

public class InventoryItem {
    private String code;
    private String productName;
    private String productType;
    private int inStock;
    private int inWarehouse;
    private int inMachine;
    private Integer unitPerCase;
    private float lastCost;
    //private InventoryItemHistoryProduct inventoryItemHistory;


    public InventoryItem(String code, String productName, String productType, int inStock, int inWarehouse, int inMachine, Integer unitPerCase) {
        if(code.charAt(0)=='P'){
            this.code=code;
        }else {
            this.code = "P-"+code;
        }
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

    public InventoryItem(String code, String productName, String productType, int inStock, int inWarehouse, int inMachine, Integer unitPerCase, float lastCost) {
        if(code.charAt(0)=='P'){
            this.code=code;
        }else {
            this.code = "P-"+code;
        }
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
        if(code.charAt(0)=='P'){
            this.code=code;
        }else {
            this.code = "P-"+code;
        }

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



    public float getLastCost() {
        return lastCost;
    }

    public void setLastCost(float lastCost) {
        this.lastCost = lastCost;
    }

    public Integer getUnitPerCase() {
        return unitPerCase;
    }

    public void setUnitPerCase(Integer unitPerCase) {
        this.unitPerCase = unitPerCase;
    }

/*
    public InventoryItemHistoryProduct getInventoryItemHistory() {
        return inventoryItemHistory;
    }

    public void setInventoryItemHistory(InventoryItemHistoryProduct inventoryItemHistory) {
        this.inventoryItemHistory = inventoryItemHistory;
    }
    */
}
