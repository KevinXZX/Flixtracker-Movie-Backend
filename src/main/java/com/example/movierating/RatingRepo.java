package com.example.movierating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepo extends JpaRepository<MovieRating, Integer> {
    List<MovieRating> findByMovieIdAndUserId(String movie_id, int user_id);
    List<MovieRating> findByUserId(int user_id);
    long deleteAllByMovieIdAndUserId(String movie_id, int user_id);
}