package com.example.bimvendpro;

import java.io.Serializable;

public class PurchaseProductClass implements Serializable {
    private String productName;
    private String pushId;
    private String productCode;
    private Integer casesPurchased;
    private Integer unitPerCase;
    private Double costPerCase;
    private Integer unitPurchased;
    private Double unitCost;
    private Double totalCost;

    PurchaseProductClass() {

    }



    PurchaseProductClass(String pushId, String productCode, String productName, Integer casesPurchased, Integer unitPerCase, Double costPerCase, Integer unitPurchased, Double unitCost, Double totalCost) {
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




    public Double getCostPerCase() {
        return costPerCase;
    }

    public void setCostPerCase(Double costPerCase) {
        this.costPerCase = costPerCase;
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

    public Integer getCasesPurchased() {
        return casesPurchased;
    }

    public void setCasesPurchased(Integer casesPurchased) {
        this.casesPurchased = casesPurchased;
    }

    public Integer getUnitPerCase() {
        return unitPerCase;
    }

    public void setUnitPerCase(Integer unitPerCase) {
        this.unitPerCase = unitPerCase;
    }

    public Integer getUnitPurchased() {
        return unitPurchased;
    }

    public void setUnitPurchased(Integer unitPurchased) {
        this.unitPurchased = unitPurchased;
    }
}
