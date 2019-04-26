package com.example.bimvendpro;

import java.util.Date;

public class MachineInstall {
    private String location;
    private Date installationDate;

    public MachineInstall(){
        location="not set";
        installationDate=null;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public Date getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }
}
