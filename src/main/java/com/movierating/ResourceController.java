package com.movierating;

import com.movierating.obj.MovieRating;
import com.movierating.obj.UserEntry;
import com.movierating.obj.requests.MovieRatingRequest;
import com.movierating.repo.UserRepo;
import com.movierating.services.RatingService;
import com.movierating.services.UserService;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class ResourceController {
  @Autowired private UserService userService;
  @Autowired private RatingService ratingService;
  @Autowired private UserRepo userRepo;

  /**
   * REST endpoint to register accounts
   *
   * @param newUserEntry the user account to be added
   * @return JSON object that shows the status of the registration
   */
  @PostMapping("/user/register")
  public ResponseEntity<Object> register(@RequestBody UserEntry newUserEntry) {
    return userService.register(newUserEntry);
  }

  /**
   * REST endpoint to login
   *
   * @param userEntry the user who wishes to login
   * @return JSON object that shows the status of the login
   */
  @PostMapping("/user/login")
  public ResponseEntity<Object> login(@RequestBody UserEntry userEntry) {
    return userService.login(userEntry);
  }

  /**
   * REST endpoint to greet the server
   *
   * @return JSON greeting
   */
  @GetMapping("/greeting")
  public ResponseEntity<Object> greeting() {
    HashMap<String, String> map = new HashMap<>();
    map.put("response", "hello");
    return ResponseEntity.ok(map);
  }

  /**
   * REST endpoint to verify the authentication token
   *
   * @return JSON object that shows whether the authentication was successful
   */
  @PostMapping("/user/welcome")
  public ResponseEntity<Object> authenticatedGreeting() {
    return ResponseEntity.ok().body("{\"response\":\"authenticated\"}");
  }

  /**
   * REST Endpoint to add/edit a movie rating
   *
   * @param movieRatingRequest authenticated request to access a movie rating
   * @return whether the adding/editing of a movie rating was successful
   */
  @PutMapping("/user/rating")
  public ResponseEntity<Object> addRating(
      @RequestBody MovieRatingRequest movieRatingRequest, HttpServletRequest request) {
    int userId = (int) request.getAttribute("user_id");
    return ratingService.rateMovie(
        new MovieRating(movieRatingRequest.getMovie_id(), userId, movieRatingRequest.getRating()));
  }

  /**
   * REST endpoint to delete a movie rating
   *
   * @param movieRatingRequest authenticated request to access a movie rating
   * @return JSON object that shows whether the deletionw as successful
   */
  @DeleteMapping("/user/rating")
  public ResponseEntity<Object> deleteRating(
      @RequestBody MovieRatingRequest movieRatingRequest, HttpServletRequest request) {
    int userId = (int) request.getAttribute("user_id");
    return ratingService.deleteRating(
        new MovieRating(movieRatingRequest.getMovie_id(), userId, movieRatingRequest.getRating()));
  }

  /**
   * REST endpoint to access movie ratings for the given user id
   *
   * @param username of the user
   * @return JSON array of all ratings for the user
   */
  @GetMapping("/user/rating/{user_name}")
  public ResponseEntity<Object> getRating(@PathVariable("user_name") String username) {
    int user_id = userService.findUserByUsername(username).getId();
    return ratingService.getRatings(user_id);
  }
  //    @PutMapping("/user")
  //    public ResponseEntity<Object>
}
