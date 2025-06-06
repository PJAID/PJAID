package com.example.pjaidmobile.data.model;

public class Device {
    private String id;
    private String name;
    private String serialNumber;
    private String purchaseDate;
    private String lastService;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getLastService() {
        return lastService;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setLastService(String lastService) {
        this.lastService = lastService;
    }
}
