package com.example.bimvendpro;

import java.util.Date;

public class InventoryItemHistoryProduct {
    private String historyDate;
    private String type;
    private String location;
    private String items;
    private String unitPerItem;
    private String totalDollar;

    public InventoryItemHistoryProduct(String type, String historyDate, String item, String unitPerItem, String location, String total){
        this.historyDate=historyDate;
        this.type=type;
        this.location=location;
        this.items=item;
        this.unitPerItem=unitPerItem;
        this.totalDollar=total;
    }

    public InventoryItemHistoryProduct(){

    }

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
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

    public void setLocation(String details) {
        this.location = details;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getUnitPerItem() {
        return unitPerItem;
    }

    public void setUnitPerItem(String unitPerItem) {
        this.unitPerItem = unitPerItem;
    }

    public String getTotalDollar() {
        return totalDollar;
    }

    public void setTotalDollar(String totalDollar) {
        this.totalDollar = totalDollar;
    }
}
