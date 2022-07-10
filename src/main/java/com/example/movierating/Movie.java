package com.example.movierating;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Movie {
    private String name;
    @Id
    private int movie_id;

    public Movie(int movie_id, String name){

        this.movie_id = movie_id;
        this.name = name;
    }

    public Movie() {

    }

    public int getId() {
        return this.movie_id;
    }

    public void setId(final int id) {
        this.movie_id = id;
    }



    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


}
