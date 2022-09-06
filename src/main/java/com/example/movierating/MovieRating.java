package com.example.movierating;

import javax.persistence.*;

@Entity
public class MovieRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int event_id;
    @Column(name = "movie_id")
    private String movieId;
    @Column(name = "user_id")
    private int userId;
    private int rating;

    public MovieRating(String movie_id, int user_id,int rating){

        this.movieId = movie_id;
        this.userId = user_id;
        this.rating = rating;
    }

    public MovieRating() {

    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(final int user_id) {
        this.userId = user_id;
    }

    public String getMovieId() {
        return this.movieId;
    }

    public void setMovieId(final String movie_id) {
        this.movieId = movie_id;
    }

}
