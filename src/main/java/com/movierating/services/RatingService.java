package com.movierating.services;

import com.movierating.obj.MovieRating;
import com.movierating.repo.RatingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RatingService {
    @Autowired
    RatingRepo ratingRepo;

    /**
     * @param movieRating object that represents the movie rating
     * @return JSON object that shows the rating was successfully added
     */
    public ResponseEntity<Object> rateMovie(MovieRating movieRating) {
        List<MovieRating> ratings = ratingRepo.findByMovieIdAndUserId(movieRating.getMovieId(), movieRating.getUserId());
        if (ratings.isEmpty() || ratings.get(0) == null) {
            //Create new rating
            ratingRepo.save(movieRating);
        } else {
            ratings.get(0).setRating(movieRating.getRating());
            ratingRepo.save(ratings.get(0));
        }
        return ResponseEntity.ok().body("{\"response\":\"success\"}");
    }

    /**
     * @param user_id id of the user
     * @return JSON array of all ratings for the user
     */
    public ResponseEntity<Object> getRatings(int user_id) {
        List<MovieRating> ratings = ratingRepo.findByUserId(user_id);

        return ResponseEntity.ok(ratings);
    }

    /**
     * @param movieRating object that represents the movie rating
     * @return JSON object that shows the rating was successfully deleted
     */
    @Transactional
    public ResponseEntity<Object> deleteRating(MovieRating movieRating) {
        ratingRepo.deleteAllByMovieIdAndUserId(movieRating.getMovieId(), movieRating.getUserId());
        return ResponseEntity.ok().body("{\"response\":\"success\"}");
    }

}
