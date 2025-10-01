package com.example.weather.model;

public class WeatherResponse {
    private String city;
    private double temperature;
    private int humidity;
    private String description;

    public WeatherResponse(){}

    public WeatherResponse(String city, double temperature, int humidity, String description){
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.description = description;
    }

    // getters & setters
    public String getCity() {return this.city;}
    public void setCity(String city) {this.city = city;}

    public double getTemperature() {return this.temperature;}
    public void setTemperature(double temperature) {this.temperature = temperature;}

    public int getHumidity() {return this.humidity;}
    public void setHumidity(int humidity) {this.humidity = humidity;}

    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;}
        
}
