package com.example.movierating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/")
public class ResourceController {
    private static final String template = "Hello, %s!";
    private static final String FAILED_AUTH_ERROR = "{\"response\":\"incorrect_access_token\"}";
    private final AtomicLong counter = new AtomicLong();
    @Autowired
    UserService userService;
    @Autowired
    UserRepo userRepo;

    @PostMapping("/user/register")
    public ResponseEntity<Object> register(@RequestBody UserEntry newUserEntry) {
        return userService.register(newUserEntry);
    }

    @GetMapping("/user/login")
    public ResponseEntity<Object> login(@RequestBody UserEntry userEntry) {
        return userService.login(userEntry);
    }
    @GetMapping("/greeting")
    public ResponseEntity<Object> greeting() {
        return ResponseEntity.ok().body("Hello");
    }
    @GetMapping("/user/welcome")
    public ResponseEntity<Object> authenticatedGreeting(@RequestBody AuthRequest authRequest) {
        System.out.println("Attempting Auth with "+authRequest.getEmail()+" : "+authRequest.getAuthToken());
        if(userService.verifyToken(authRequest.getEmail(), authRequest.getAuthToken())){
            return ResponseEntity.ok().body("{\"response\":\"authenticated\"}");
        }
        return ResponseEntity.ok().body(FAILED_AUTH_ERROR);
    }
//    @PutMapping("/user")
//    public ResponseEntity<Object>
}

