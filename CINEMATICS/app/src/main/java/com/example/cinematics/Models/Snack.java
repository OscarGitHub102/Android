package com.example.cinematics.Models;
public class Snack {
    private int idSnack;
    private String URL;
    private String name;
    private double price;
    public Snack(int idSnack,String URL,String name, double price) {
        this.idSnack = idSnack;
        this.name = name;
        this.URL = URL;
        this.price = price;
    }
    public int getIdSnack() {return idSnack;}
    public String getURL() {return URL;}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
}