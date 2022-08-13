package com.example.movierating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

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

    public ResponseEntity<Object> login(User user){
        if(userRepo.findByEmail(user.getEmail()).size()==0){
            return ResponseEntity.status(404).body("Account does not exist");
        }
        String correctPassword = userRepo.findByEmail(user.getEmail()).get(0).getPassword();
        if(encoder.matches(user.getPassword(), correctPassword) ){
            byte[] randomBytes = new byte[64];
            secureRandom.nextBytes(randomBytes);
            tokenInMemoryRepo.addToken(user.getEmail(),base64Encoder.encodeToString(randomBytes));
            return ResponseEntity.ok().body("{\"access_token\":"+base64Encoder.encodeToString(randomBytes) +"}");
        }
        return ResponseEntity.ok().body("Incorrect password");
    }

    public boolean verifyToken(String email, String token){
        System.out.println("Checking Token");
        return tokenInMemoryRepo.verifyToken(email,token);
    }

}
