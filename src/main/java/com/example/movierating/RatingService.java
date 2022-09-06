package com.example.movierating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class RatingService {

    @Autowired
    UserRepo userRepo;
    static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
    private final AccessTokenInMemoryRepo tokenInMemoryRepo = new AccessTokenInMemoryRepo();

    public static String generateHash(String password){
        return encoder.encode(password);
    }

    public boolean validPassword(String dbPassword, String incPassword){
        return encoder.matches(incPassword,dbPassword);
    }

    public ResponseEntity<Object> register(UserEntry newUserEntry){
        if(userRepo.findByName(newUserEntry.getName()).size()!=0){
            return ResponseEntity.status(409).body("Name already used");
        }else if( userRepo.findByEmail(newUserEntry.getEmail()).size()!=0){
            return ResponseEntity.status(409).body("Email already used");
        }
        newUserEntry.setPassword(generateHash(newUserEntry.getPassword()));
        userRepo.save(newUserEntry);
        return ResponseEntity.ok().body("User created");
    }

    public ResponseEntity<Object> login(UserEntry userEntry){
        if(userRepo.findByEmail(userEntry.getEmail()).size()==0){
            return ResponseEntity.status(404).body("Account does not exist");
        }
        String correctPassword = userRepo.findByEmail(userEntry.getEmail()).get(0).getPassword();
        if(encoder.matches(userEntry.getPassword(), correctPassword) ){
            byte[] randomBytes = new byte[64];
            secureRandom.nextBytes(randomBytes);
            tokenInMemoryRepo.addToken(userEntry.getEmail(),base64Encoder.encodeToString(randomBytes));
            return ResponseEntity.ok().body("{\"access_token\":"+base64Encoder.encodeToString(randomBytes) +"}");
        }
        return ResponseEntity.ok().body("Incorrect password");
    }

    public boolean verifyToken(String email, String token){
        System.out.println("Checking Token");
        return tokenInMemoryRepo.verifyToken(email,token);
    }

}
