package com.example.bimvendpro;

import java.util.ArrayList;

public class InventoryItemHistory {

    private ArrayList<InventoryItemHistorySingle>  inventoryItemHistory=new ArrayList<>();

    public InventoryItemHistory(ArrayList<InventoryItemHistorySingle> list){
        this.inventoryItemHistory=list;
    }

    public void addNewHistory(ArrayList<InventoryItemHistorySingle> historyList){
        this.inventoryItemHistory=historyList;
    }

    public void addNewHistory(InventoryItemHistorySingle history){
        inventoryItemHistory.add(history);
    }
    public void addNewHistory(InventoryItemHistorySingle[] history){
        for(InventoryItemHistorySingle c:history){
            inventoryItemHistory.add(c);
        }
    }
    public ArrayList<InventoryItemHistorySingle> getInventoryItemHistory(){
        return inventoryItemHistory;
    }

    public int getSize(){
        return inventoryItemHistory.size();
    }
}
