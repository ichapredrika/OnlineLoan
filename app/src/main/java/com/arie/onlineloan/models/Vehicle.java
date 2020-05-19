package com.arie.onlineloan.models;

public class Vehicle {
    private String type;
    private String brand;
    private String model;
    private String year;
    private double price;

    public Vehicle(String type, String brand, String model, String year, double price) {
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
