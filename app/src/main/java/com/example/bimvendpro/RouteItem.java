package com.example.bimvendpro;

import android.widget.ListView;

import java.util.List;

public class RouteItem {

    private String name;
    private String description;
    private List<LicationItem> locationsList;

    private int noOfLocation;
    private int noOfMachines;



    public RouteItem(String name, String description, List<LicationItem> locationsList) {
        this.name = name;
        this.description = description;
        this.locationsList = locationsList;
        this.noOfLocation = 0;
        this.noOfMachines = 0;
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

    public List<LicationItem> getLocationsList() {
        return locationsList;
    }

    public void setLocationsList(List<LicationItem> locationsList) {
        this.locationsList = locationsList;
    }
}
