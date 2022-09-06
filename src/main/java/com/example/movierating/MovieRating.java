package com.example.movierating;

import javax.persistence.*;

@Entity
public class MovieRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int event_id;
    @Id
    private int movie_id;
    @Id
    private int user_id;
    private int rating;

    public MovieRating(int movie_id, int user_id,int rating){

        this.movie_id = movie_id;
        this.user_id = user_id;
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

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(final int user_id) {
        this.user_id = user_id;
    }

    public int getMovie_id() {
        return this.movie_id;
    }

    public void setMovie_id(final int movie_id) {
        this.movie_id = movie_id;
    }

}
