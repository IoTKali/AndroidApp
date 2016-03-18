package com.iotkali.karport;

public class Car {

    private String plates, brand, model;
    private int year;
    public Car(String plates, String brand, String model, int year) {
        this.plates = plates;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }
    public String getPlates() {
        return plates;
    }
    public void setPlates(String plates) {
        this.plates = plates;
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
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
}