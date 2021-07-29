package com.telemetry.configclient.dao;

public class Data {
    private String id;
    private String temperature;
    private String humidity;
    private String location;

    public Data() {
    }

    public Data(String id, String temperature, String humidity, String location) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
