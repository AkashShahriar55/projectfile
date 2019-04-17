package com.example.bimvendpro;

public class LicationItem {

    //basic info
    private String status;
    private String code;
    private String name;
    private String Address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String location;



    //contact details
    private String contactName;
    private String contactPhone;
    private String contactEmail;

    //commission and tax
    private String commissionType;
    private int commission;
    private String taxType;
    private  int tax;

    //working hour
    private String workingHour;

    //installed machine
    private String machineCode;
    private int noOfMachines;

    //service pattern
    private int intervalDay;
    private String theDay;

    public LicationItem(String status , String code, String name, String address, String city, String state, String zip, String country, String location, String contactName, String contactPhone, String contactEmail) {
        this.status = "Active";
        this.code = code;
        this.name = name;
        Address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.location = location;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public String getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(String workingHour) {
        this.workingHour = workingHour;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public int getNoOfMachines() {
        return noOfMachines;
    }

    public void setNoOfMachines(int noOfMachines) {
        this.noOfMachines = noOfMachines;
    }

    public int getIntervalDay() {
        return intervalDay;
    }

    public void setIntervalDay(int intervalDay) {
        this.intervalDay = intervalDay;
    }

    public String getTheDay() {
        return theDay;
    }

    public void setTheDay(String theDay) {
        this.theDay = theDay;
    }
}
