package com.example.cinematics.Models;
public class Dessert {
    private int idDessert;
    private String URL;
    private String name;
    private double price;
    public Dessert(int idDessert, String URL, String name, double price) {
        this.idDessert = idDessert;
        this.URL = URL;
        this.name = name;
        this.price = price;
    }
    public int getIdDessert() {return idDessert;}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public String getURL() {return URL;}
}