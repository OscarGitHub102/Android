package com.example.cinematics.Models;
public class Drink {
    private int idDrink;
    private String URL;
    private String name;
    private double price;
    public Drink(int idDrink, String URL,String name, double price) {
        this.idDrink = idDrink;
        this.name = name;
        this.URL = URL;
        this.price = price;
    }
    public int getIdDrink() {return idDrink;}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public String getURL() {return URL;}
}