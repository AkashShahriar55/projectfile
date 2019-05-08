package com.example.bimvendpro;

import java.io.Serializable;

public class PurchaseClass implements Serializable {
    private String supplier;

    private String purchaseDate;
    private Integer productNo=0;
    private Double totalCost=0.0;

    PurchaseClass(){

    }

    PurchaseClass(String supplier, String purchaseDate){
        this.supplier=supplier;
        this.purchaseDate=purchaseDate;
    }

    PurchaseClass(String supplier, String purchaseDate, Integer productNo, Double totalCost){
        this.supplier=supplier;
        this.purchaseDate=purchaseDate;
        this.productNo=productNo;
        this.totalCost=totalCost;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getProductNo() {
        return productNo;
    }

    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}
