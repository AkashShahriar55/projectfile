package com.example.bimvendpro;

import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

public class RouteItem implements Serializable {

    private String name;
    private String description;
    private List<Location> locationsList;
    private List<String> locationCode;

    public List<String> getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(List<String> locationCode) {
        this.locationCode = locationCode;
    }

    private int noOfLocation;
    private int noOfMachines;



    public RouteItem(String name, String description, List<String> locationCode,int noOfLocation,int noOfMachines) {
        this.name = name;
        this.description = description;
        this.locationCode = locationCode;
        this.noOfLocation = noOfLocation;
        this.noOfMachines = noOfMachines;
    }

    public RouteItem() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Location> getLocationsList() {
        return locationsList;
    }

    public void setLocationsList(List<Location> locationsList) {
        this.locationsList = locationsList;
    }
}
