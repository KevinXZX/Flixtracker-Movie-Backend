package com.example.movierating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.security.SecureRandom;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;
    static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String generateHash(String password){
        return encoder.encode(password);
    }

    public boolean validPassword(String dbPassword, String incPassword){
        return encoder.matches(incPassword,dbPassword);
    }

    public ResponseEntity<Object> register(User newUser){
        if(userRepo.findByName(newUser.getName()).size()!=0){
            return ResponseEntity.status(409).body("Name already used");
        }else if( userRepo.findByEmail(newUser.getEmail()).size()!=0){
            return ResponseEntity.status(409).body("Email already used");
        }
        newUser.setPassword(generateHash(newUser.getPassword()));
        userRepo.save(newUser);
        return ResponseEntity.ok().body("User created");
    }

}
