package com.example.bimvendpro;

public class MachineIngredients {
    private String code;
    private String name;
    private Integer canister;
    private Integer max;


    MachineIngredients(String code, String name, Integer canister, Integer max){
        this.canister=canister;
        this.code=code;
        this.name=name;
        this.max=max;
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

    public Integer getCanister() {
        return canister;
    }

    public void setCanister(Integer canister) {
        this.canister = canister;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
