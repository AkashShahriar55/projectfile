package com.example.bimvendpro;

import java.util.Date;

public class InventoryItemHistorySingle {
    private Date historyDate;
    private String type;
    private String details;

    public InventoryItemHistorySingle(Date historyDate, String type, String details){
        this.historyDate=historyDate;
        this.type=type;
        this.details=details;
    }

    public Date getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(Date historyDate) {
        this.historyDate = historyDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
