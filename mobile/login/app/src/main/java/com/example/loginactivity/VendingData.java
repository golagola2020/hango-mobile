package com.example.loginactivity;


public class VendingData {
    private String VendingName;
    private String VendingDescription;
    private String VendingSerialNumber;
    private int VendingFullsize;

    public void setVendingName(String vendingName) {
        VendingName = vendingName;
    }

    public void setVendingDescription(String vendingDescription) {
        VendingDescription = vendingDescription;
    }

    public void setVendingSerialNumber(String vendingSerialNumber) {
        VendingSerialNumber = vendingSerialNumber;
    }
    public void setVendingFullsize(int vendingFullsize){
        VendingFullsize = vendingFullsize;
    }

    public String getVendingName() {
        return VendingName;
    }

    public String getVendingDescription() {
        return VendingDescription;
    }

    public String getVendingSerialNumber() {
        return VendingSerialNumber;
    }

    public int getVendingFullsize(){
        return VendingFullsize;
    }
}

