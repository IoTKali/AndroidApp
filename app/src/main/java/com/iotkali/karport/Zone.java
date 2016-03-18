package com.iotkali.karport;

import java.util.List;

public class Zone {

    private String name, strokeColor;
    private int numPoints, totalSpaces, cars, availableSpaces;
    private double[] center;
    private List<double[]> coordinates;
    private int fillColor[];
    private boolean clickable;

    public double[] getCenter() {
        return center;
    }

    public void setCenter(double[] center) {
        this.center = center;
    }

    public Zone(String name, int numPoints, int totalSpaces, int cars, double[] center, List<double[]> coordinates, int availableSpaces, String strokeColor, int fillColor[], boolean clickable){
        this.name = name;
        this.numPoints = numPoints;
        this.totalSpaces = totalSpaces;
        this.cars = cars;
        this.center = center;
        this.coordinates = coordinates;
        this.availableSpaces = availableSpaces;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.clickable = clickable;

    }

    public int getCars() {
        return cars;
    }

    public void setCars(int cars) {
        this.cars = cars;
    }

    public int getTotalSpaces() {
        return totalSpaces;
    }

    public void setTotalSpaces(int totalSpaces) {
        this.totalSpaces = totalSpaces;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    public void setAvailableSpaces(int availableSpaces) {
        this.availableSpaces = availableSpaces;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public int[] getFillColor() {
        return fillColor;
    }

    public void setFillColor(int[] fillColor) {
        this.fillColor = fillColor;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(short numPoints) {
        this.numPoints = numPoints;
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<double[]> coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailableSpaces() {
        return availableSpaces;
    }

    public void setAvailableSpaces(short availableSpaces) {
        this.availableSpaces = availableSpaces;
    }

}