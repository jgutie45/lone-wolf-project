package com.example.lonewolf;

class ParkReportModel {

    private String id;
    private String fullName;
    private String parkCode;
    private String description;
    private Double latitude;
    private Double longitude;
    private String latLong;
    private String states;
    private String directionsInfo;

    public ParkReportModel(String id, String fullName, String parkCode, String description, Double latitude, Double longitude, String latLong, String states, String directionsInfo) {
        this.id = id;
        this.fullName = fullName;
        this.parkCode = parkCode;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latLong = latLong;
        this.states = states;
        this.directionsInfo = directionsInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getDirectionsInfo() {
        return directionsInfo;
    }

    public void setDirectionsInfo(String directionsInfo) {
        this.directionsInfo = directionsInfo;
    }

    public ParkReportModel() {
    }

    @Override
    public String toString() {
        return fullName + "\n" +
                "latLong: " + latLong + "\n" +
                "States/State Park is Located: " + states + "\n";
        //"Description: " + description + "\n" +
    }

}
