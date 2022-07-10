package com.example.movierating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/public")
public class ResourceController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    @Autowired
    UserService userService;
    @Autowired
    UserRepo userRepo;

    @PostMapping("/user/register")
    public ResponseEntity<Object> greeting(@RequestBody User newUser) {
        return userService.register(newUser);
    }
    @GetMapping("/greeting")
    public ResponseEntity<Object> greeting() {
        return ResponseEntity.ok().body("Hello");
    }
//    @PutMapping("/user")
//    public ResponseEntity<Object>
}

