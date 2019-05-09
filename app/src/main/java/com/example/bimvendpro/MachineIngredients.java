package com.example.bimvendpro;

import java.io.Serializable;

public class MachineIngredients implements Serializable {
    private String code;
    private String name;
    private Double vendPrice;
    private Integer max;
    private Integer lastCount;


    MachineIngredients(String code, String name, Double vendPrice, Integer max, Integer lastCount){
        this.vendPrice=vendPrice;
        this.code=code;
        this.name=name;
        this.max=max;
        this.lastCount=lastCount;
    }

    MachineIngredients(){

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



    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Double getVendPrice() {
        return vendPrice;
    }

    public void setVendPrice(Double vendPrice) {
        this.vendPrice = vendPrice;
    }

    public Integer getLastCount() {
        return lastCount;
    }

    public void setLastCount(Integer lastCount) {
        this.lastCount = lastCount;
    }
}
