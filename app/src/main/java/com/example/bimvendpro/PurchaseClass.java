package com.example.bimvendpro;

import java.io.Serializable;
import java.util.Map;

public class PurchaseClass implements Serializable {
    private String supplier;
    private String pushId;
    private String purchaseDate;
    private Integer productNo = 0;
    private Double totalCost = 0.0;
    private Map<String, PurchaseProductClass> purchaseProducts;

    PurchaseClass() {

    }

    PurchaseClass(String pushId, String supplier, String purchaseDate) {
        this.supplier = supplier;
        this.pushId = pushId;
        this.purchaseDate = purchaseDate;
    }

    PurchaseClass(String pushId, String supplier, String purchaseDate, Integer productNo, Double totalCost, Map<String, PurchaseProductClass> purchaseProducts) {
        this.supplier = supplier;
        this.pushId = pushId;
        this.purchaseDate = purchaseDate;
        this.productNo = productNo;
        this.totalCost = totalCost;
        this.purchaseProducts = purchaseProducts;
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

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public Map<String, PurchaseProductClass> getPurchaseProducts() {
        return purchaseProducts;
    }

    public void setPurchaseProducts(Map<String, PurchaseProductClass> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
    }
}
