package com.movierating.obj.requests;

import com.sun.istack.NotNull;

public class MovieRatingRequest {
    @NotNull
    private String movie_id;

    @NotNull
    private int rating;

    public MovieRatingRequest() {

    }

    /**
     * Generates an authenticated request to add a movie rating
     *
     * @param movie_id name of the movie
     * @param rating   rating given
     */
    public MovieRatingRequest(String movie_id, int rating) {
        this.movie_id = movie_id;
        this.rating = rating;
    }

    public MovieRatingRequest(String movie_id) {
        this.movie_id = movie_id;
        this.rating = 0;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
