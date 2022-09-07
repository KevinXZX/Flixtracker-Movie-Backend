package com.example.movierating;

import com.sun.istack.NotNull;

public class MovieRatingRequest extends AuthRequest{
    @NotNull
    private String movie_id;

    @NotNull
    private int rating;

    public MovieRatingRequest() {

    }

    public MovieRatingRequest(String email,String authToken,String movie_id, int rating){
        super(email,authToken);
        this.movie_id = movie_id;
        this.rating = rating;
    }

    public MovieRatingRequest(String email,String authToken,String movie_id){
        super(email,authToken);
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
