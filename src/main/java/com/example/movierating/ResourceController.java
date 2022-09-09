package com.example.movierating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/")
public class ResourceController {
    private static final String FAILED_AUTH_ERROR = "{\"response\":\"incorrect_access_token\"}";
    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserRepo userRepo;

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
    @GetMapping("/user/login")
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
        map.put("response","hello");
        return ResponseEntity.ok(map);
    }

    /**
     * REST endpoint to verify the authentication token
     *
     * @param authRequest request to be authenticated
     * @return JSON object that shows whether the authentication was successful
     */
    @GetMapping("/user/welcome")
    public ResponseEntity<Object> authenticatedGreeting(@RequestBody AuthRequest authRequest) {
        if(userService.verifyToken(authRequest.getEmail(), authRequest.getAuthToken())){
            return ResponseEntity.ok().body("{\"response\":\"authenticated\"}");
        }
        return ResponseEntity.ok().body(FAILED_AUTH_ERROR);
    }

    /**
     * REST Endpoint to add/edit a movie rating
     *
     * @param movieRatingRequest authenticated request to access a movie rating
     * @return whether the adding/editing of a movie rating was successful
     */
    @PutMapping("/user/rating")
    public ResponseEntity<Object> addRating(@RequestBody MovieRatingRequest movieRatingRequest) {
        if(userService.verifyToken(movieRatingRequest.getEmail(), movieRatingRequest.getAuthToken())){
            int userId = userRepo.findByEmail(movieRatingRequest.getEmail()).get(0).getId();
            return ratingService.rateMovie(new MovieRating(movieRatingRequest.getMovie_id(),userId,movieRatingRequest.getRating()));
        }
        return ResponseEntity.ok().body(FAILED_AUTH_ERROR);
    }

    /**
     * REST endpoint to delete a movie rating
     *
     * @param movieRatingRequest authenticated request to access a movie rating
     * @return JSON object that shows whether the deletionw as successful
     */
    @DeleteMapping("/user/rating")
    public ResponseEntity<Object> deleteRating(@RequestBody MovieRatingRequest movieRatingRequest) {
        if(userService.verifyToken(movieRatingRequest.getEmail(), movieRatingRequest.getAuthToken())){
            int userId = userRepo.findByEmail(movieRatingRequest.getEmail()).get(0).getId();
            return ratingService.deleteRating(new MovieRating(movieRatingRequest.getMovie_id(),userId,movieRatingRequest.getRating()));
        }
        return ResponseEntity.ok().body(FAILED_AUTH_ERROR);
    }

    /**
     * REST endpoint to access movie ratings for the given user id
     *
     * @param user_id id of the user
     * @return JSON array of all ratings for the user
     */
    @GetMapping("/user/rating/{user_id}")
    public ResponseEntity<Object> getRating(@PathVariable("user_id") int user_id) {
        return ratingService.getRatings(user_id);
    }
//    @PutMapping("/user")
//    public ResponseEntity<Object>
}

