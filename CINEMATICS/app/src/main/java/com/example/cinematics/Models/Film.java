package com.example.cinematics.Models;

import java.util.List;
public class Film {
    private int idFilm;
    private String URL;
    private String title;
    private int year;
    private String director;
    private List<String> generos;
    private String sinopsis;
    private List<String> reparto;
    private double rating;
    private List<String> reviews;
    public Film(int idFilm, String URL, String title, int year, String director, List<String> generos, String sinopsis, List<String> reparto, double rating, List<String> reviews) {
        this.idFilm = idFilm;
        this.URL = URL;
        this.title = title;
        this.year = year;
        this.director = director;
        this.generos = generos;
        this.sinopsis = sinopsis;
        this.reparto = reparto;
        this.rating = rating;
        this.reviews = reviews;
    }
    public int getIdFilm() {
        return idFilm;
    }
    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }
    public String getDirector() {
        return director;
    }
    public List<String> getGeneros() {
        return generos;
    }
    public String getSinopsis() {
        return sinopsis;
    }
    public List<String> getReparto() {
        return reparto;
    }
    public double getRating() {
        return rating;
    }
    public List<String> getReviews() {
        return reviews;
    }
    public String getURL() { return URL; }
}