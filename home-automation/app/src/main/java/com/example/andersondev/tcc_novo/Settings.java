package com.example.andersondev.tcc_novo;

public class Settings {

    int id;
    String bluetooth_mac;
    Double temperature;
    Boolean isInHome;
    public Settings(){}

    public Settings(int id, String bluetooth_mac, Double temperature, Boolean isInHome){
        this.id = id;
        this.bluetooth_mac = bluetooth_mac;
        this.temperature   = temperature;
        this.isInHome      = isInHome;
    }

    public Settings(String bluetooth_mac, Double temperature, Boolean isInHome){
        this.bluetooth_mac  = bluetooth_mac;
        this.temperature    = temperature;
        this.isInHome       = isInHome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBluetooth_mac() {
        return bluetooth_mac;
    }

    public void setBluetooth_mac(String bluetooth_mac) {
        this.bluetooth_mac = bluetooth_mac;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Boolean getInHome() {
        return isInHome;
    }

    public void setInHome(Boolean inHome) {
        isInHome = inHome;
    }
}
