package com.chagay.gettexid.model.entities;

public class Travel {

    public enum TRAVEL_STATUS {AVAILABLE,OCCUPIED,FINISHED}

    //---------------------------------- Fields ------------------------------------------//
    private TRAVEL_STATUS travel_status;
    private String startLocation;
    private String endLocation;
    private String startTravelTime;
    private String endTravelTime;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmailAddress;

    //---------------------------------- Constructors ----------------------------------//

    public Travel(String startLocation, String endLocation, String startTravelTime
            , String customerName, String customerPhoneNumber, String customerEmailAdress) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTravelTime = startTravelTime;
        this.endTravelTime = "-1";
        travel_status=TRAVEL_STATUS.AVAILABLE;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerEmailAddress = customerEmailAdress;
    }

    public Travel(){
    }

    //------------------------------- Getters and Setters ------------------------------//
    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartTravelTime() {
        return startTravelTime;
    }

    public void setStartTravelTime(String startTravelTime) {
        this.startTravelTime = startTravelTime;
    }

    public String getEndTravelTime() {
        return endTravelTime;
    }

    public void setEndTravelTime(String endTravelTime) {
        this.endTravelTime = endTravelTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerEmailAddress() {
        return customerEmailAddress;
    }

    public void setCustomerEmailAddress(String customerEmailAdress) {
        this.customerEmailAddress = customerEmailAdress;
    }

    public TRAVEL_STATUS getTravel_status() {
        return travel_status;
    }

    public void setTravel_status(TRAVEL_STATUS travel_status) {
        this.travel_status = travel_status;
    }

}

