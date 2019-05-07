package com.example.bimvendpro;

import java.io.Serializable;

public class DriverImages implements Serializable {
    private String imgUrl;

    public DriverImages() {
    }

    public DriverImages( String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
