package com.example.bimvendpro;

import java.io.Serializable;
import java.util.List;

public class ExpenseItem implements Serializable {

    private String Date;
    private String Catagory;
    private String Amount;
    private String Payee;
    private String LocationName;
    private String Drescription;
    private String Attachment;
    private String Code;

    public ExpenseItem() {
    }

    public ExpenseItem(String date, String catagory, String amount, String payee, String locationName, String drescription, String attachment,String code) {
        Date = date;
        Catagory = catagory;
        Amount = amount;
        Payee = payee;
        LocationName = locationName;
        Drescription = drescription;
        Attachment = attachment;
        Code = code;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCatagory() {
        return Catagory;
    }

    public void setCatagory(String catagory) {
        Catagory = catagory;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPayee() {
        return Payee;
    }

    public void setPayee(String payee) {
        Payee = payee;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getDrescription() {
        return Drescription;
    }

    public void setDrescription(String drescription) {
        Drescription = drescription;
    }

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }
}
