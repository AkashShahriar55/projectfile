package com.example.bimvendpro;

import java.io.Serializable;

public class PurchaseProductClass implements Serializable {
    private String productName;
    private String pushId;
    private String productCode;
    private Double casesPurchased;
    private Double unitPerCase;
    private Double costPerCase;
    private Double unitPurchased;
    private Double unitCost;
    private Double totalCost;

    PurchaseProductClass() {

    }



    PurchaseProductClass(String pushId, String productCode, String productName, Double casesPurchased, Double unitPerCase, Double costPerCase, Double unitPurchased, Double unitCost, Double totalCost) {
        this.productCode = productCode;
        this.productName = productName;
        this.pushId = pushId;
        this.casesPurchased = casesPurchased;
        this.unitPerCase = unitPerCase;
        this.costPerCase = costPerCase;
        this.unitPurchased = unitPurchased;
        this.unitCost=unitCost;
        this.totalCost=totalCost;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Double getCasesPurchased() {
        return casesPurchased;
    }

    public void setCasesPurchased(Double casesPurchased) {
        this.casesPurchased = casesPurchased;
    }

    public Double getUnitPerCase() {
        return unitPerCase;
    }

    public void setUnitPerCase(Double unitPerCase) {
        this.unitPerCase = unitPerCase;
    }

    public Double getCostPerCase() {
        return costPerCase;
    }

    public void setCostPerCase(Double costPerCase) {
        this.costPerCase = costPerCase;
    }

    public Double getUnitPurchased() {
        return unitPurchased;
    }

    public void setUnitPurchased(Double unitPurchased) {
        this.unitPurchased = unitPurchased;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }


    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
