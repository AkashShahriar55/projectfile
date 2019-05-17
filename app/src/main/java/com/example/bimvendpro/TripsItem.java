package com.example.bimvendpro;

import java.io.Serializable;
import java.util.List;

public class TripsItem implements Serializable {
    private String driverName;
    private String tripName;
    private String tripDate;
    private String tripDescription;
    private double cashCollected;
    private int noOfLocation;
    private int noOfMachines;
    private String status;
    private String tripNumber;
    private List<TripMachines> tripMachines;
    private String Locations;
    private String Machines;

    public TripsItem(String driverName, String tripDate, String tripDescription, int noOfLocation, int noOfMachines, String tripNumber, List<TripMachines> tripMachines,String locations,String machines,String tripName) {
        this.driverName = driverName;
        this.tripDate = tripDate;
        this.tripDescription = tripDescription;
        this.cashCollected = 0.0;
        this.noOfLocation = noOfLocation;
        this.noOfMachines = noOfMachines;
        this.status = "created";
        this.tripNumber = tripNumber;
        this.tripMachines = tripMachines;
        this.Locations = locations;
        this.Machines = machines;
        this.tripName = tripName;
    }

    public TripsItem() {
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getLocations() {
        return Locations;
    }

    public void setLocations(String locations) {
        Locations = locations;
    }

    public String getMachines() {
        return Machines;
    }

    public void setMachines(String machinnes) {
        Machines = machinnes;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    public double getCashCollected() {
        return cashCollected;
    }

    public void setCashCollected(double cashCollected) {
        this.cashCollected = cashCollected;
    }

    public int getNoOfLocation() {
        return noOfLocation;
    }

    public void setNoOfLocation(int noOfLocation) {
        this.noOfLocation = noOfLocation;
    }

    public int getNoOfMachines() {
        return noOfMachines;
    }

    public void setNoOfMachines(int noOfMachines) {
        this.noOfMachines = noOfMachines;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(String tripNumber) {
        this.tripNumber = tripNumber;
    }

    public List<TripMachines> getTripMachines() {
        return tripMachines;
    }

    public void setTripMachines(List<TripMachines> tripMachines) {
        this.tripMachines = tripMachines;
    }
}
