package com.arie.onlineloan.models;

import org.json.JSONObject;

public class Vehicle {
    private String id;
    private String type;
    private String brand;
    private String model;
    private String year;
    private double price;


    public Vehicle(JSONObject object) {
        try {
            this.id = object.getString("ID");
            this.type = object.getString("TYPE");
            this.brand = object.getString("BRAND");
            this.model = object.getString("MODEL");
            this.year = object.getString("YEAR");
            this.price = Double.parseDouble(object.getString("PRICE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vehicle(String id, String type, String brand, String model, String year, double price) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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