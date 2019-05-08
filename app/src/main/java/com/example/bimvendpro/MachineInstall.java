package com.example.bimvendpro;

import java.io.Serializable;
import java.util.Date;

public class MachineInstall implements Serializable {
    private String location;
    private String installationDate;

    public MachineInstall(){
        location="not set";
        installationDate=null;
    }
    public MachineInstall(String location, String installationDate){
        this.location=location;
        this.installationDate=installationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(String installationDate) {
        this.installationDate = installationDate;
    }
}
