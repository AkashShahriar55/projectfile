package com.example.bimvendpro;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable {

    //basic info
    private String status;
    private String code;
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
    private float commission;
    private  float tax;
    private String notes;

    //Machines and routes
    private List<String> machineCodes;
    private List<String> routes;

    //working hour
    private String workingHour;

    public float getCommission() {
        return commission;
    }

    public float getTax() {
        return tax;
    }

    //installed machine
    private int noOfMachines;

    //service pattern
    private int intervalDay;
    private String theDay;

    private String lastVisit;
    private String nextVisit;

    private double longitude;
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getNextVisit() {
        return nextVisit;
    }

    public void setNextVisit(String nextVisit) {
        this.nextVisit = nextVisit;
    }

    public Location(String status, String code, String address, String city, String state, String zip, String country, String location, String contactName, String contactPhone, String contactEmail, String commissionType, float commission, float tax, String workingHour, int intervalDay, String theDay, double longitude, double latitude, String notes) {
        this.status = status;
        this.code = code;
        Address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.location = location;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.commissionType = commissionType;
        this.commission = commission;
        this.tax = tax;
        this.workingHour = workingHour;
        this.intervalDay = intervalDay;
        this.theDay = theDay;
        this.lastVisit = "00-00-00";
        this.nextVisit = "00-00-00";
        this.noOfMachines = 0;
        this.longitude = longitude;
        this.latitude = latitude;
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public Location(String code, String location, int noOfMachines, double longitude, double latitude) {
        this.code = code;
        this.location = location;
        this.noOfMachines = noOfMachines;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Location() {

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


    public String getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(String workingHour) {
        this.workingHour = workingHour;
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
