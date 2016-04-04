package com.techgenie.velosafe;

/*
    Class List Definitions
    Purpose:
    Defines a structure for storing the data to be displayed in the list view
 */
public class ListDefinitions {
    private String Places = "";
    private String Distance = "";
    private  String Colour ="";

    public void setPlaces(String Places) {
        this.Places = Places;
    }

    public String getPlaces() {
        return Places;
    }

    public void setDistance(String Distance) {
        this.Distance = Distance;
    }

    public String getDistance() {
        return Distance;
    }

    public void setColour(String Colour) {
        this.Colour = Colour;
    }

    public String getColour() {
        return Colour;
    }


}
