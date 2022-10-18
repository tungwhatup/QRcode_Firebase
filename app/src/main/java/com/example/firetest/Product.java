package com.example.firetest;

public class Product {
    private String name;
    private String code;
    private String timeSubmit;
    private String quantity;
    private String location;

    public Product(){}

    public Product(String name, String code, String timeSubmit, String quantity, String location) {
        this.name = name;
        this.code = code;
        this.timeSubmit = timeSubmit;
        this.quantity = quantity;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimeSubmit() {
        return timeSubmit;
    }

    public void setTimeSubmit(String timeSubmit) {
        this.timeSubmit = timeSubmit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
