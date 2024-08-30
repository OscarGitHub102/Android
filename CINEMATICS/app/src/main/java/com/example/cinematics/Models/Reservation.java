package com.example.cinematics.Models;

import java.util.ArrayList;
import java.util.Date;

public class Reservation
{
    private int id;
    private Film film;
    private ArrayList<Snack> ReservationSnack;
    private ArrayList<Drink> ReservationDrink;
    private ArrayList<Dessert> ReservationDessert;
    private double finalPrice;
    private Date date;
    public Reservation(int id,Film film, ArrayList<Snack> reservationSnack, ArrayList<Drink> reservationDrink, ArrayList<Dessert> reservationDessert, double finalPrice, Date date) {
        this.id = id;
        this.film = film;
        ReservationSnack = reservationSnack;
        ReservationDrink = reservationDrink;
        ReservationDessert = reservationDessert;
        this.finalPrice = finalPrice;
        this.date = date;

    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public Film getFilm() {return film;}
    public ArrayList<Snack> getReservationSnack() {return ReservationSnack;}
    public ArrayList<Drink> getReservationDrink() {return ReservationDrink;}
    public ArrayList<Dessert> getReservationDessert() {return ReservationDessert;}
    public double getFinalPrice() {return finalPrice;}
    public Date getDate() {return date;}
}