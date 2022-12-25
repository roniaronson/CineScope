package com.example.retrofitwapi;

public class Movie {

    private String title, poster, overView;
    private Double rating;
    private String URL = "https://image.tmdb.org/t/p/w500";

    public Movie(String title, String poster, String overView, Double rating) {
        this.title = title;
        this.poster = URL + poster;
        this.overView = overView;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverView() {
        return overView;
    }

    public Double getRating() {
        return rating;
    }
}
