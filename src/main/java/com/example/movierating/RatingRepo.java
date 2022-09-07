package com.example.movierating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepo extends JpaRepository<MovieRating, Integer> {
    /**
     * @param movie_id
     * @param user_id
     * @return List of MovieRatings that have the movie_id and user_id
     */
    List<MovieRating> findByMovieIdAndUserId(String movie_id, int user_id);

    /**
     * @param user_id
     * @return List of MovieRatings that have the user_id
     */
    List<MovieRating> findByUserId(int user_id);

    /**
     * @param movie_id
     * @param user_id
     * @return number of ratings deleted
     */
    long deleteAllByMovieIdAndUserId(String movie_id, int user_id);
}