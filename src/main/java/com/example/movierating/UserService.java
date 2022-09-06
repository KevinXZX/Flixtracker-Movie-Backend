package com.example.movierating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

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

    public ResponseEntity<Object> register(UserEntry newUserEntry){
        HashMap<String, String> map = new HashMap<>();
        if(userRepo.findByName(newUserEntry.getName()).size()!=0){
            map.put("response","Name already used");
            return ResponseEntity.status(409).body(map);
        }else if( userRepo.findByEmail(newUserEntry.getEmail()).size()!=0){
            map.put("response","Email already used");
            return ResponseEntity.status(409).body(map);
        }
        newUserEntry.setPassword(generateHash(newUserEntry.getPassword()));
        userRepo.save(newUserEntry);
        map.put("response","User created");
        return ResponseEntity.ok(map);
    }

    public ResponseEntity<Object> login(UserEntry userEntry){
        if(userRepo.findByEmail(userEntry.getEmail()).isEmpty()){
            return ResponseEntity.status(404).body("Account does not exist");
        }
        final String correctPassword = userRepo.findByEmail(userEntry.getEmail()).get(0).getPassword();
        if(encoder.matches(userEntry.getPassword(), correctPassword) ){
            byte[] randomBytes = new byte[64];
            secureRandom.nextBytes(randomBytes);
            tokenInMemoryRepo.addToken(userEntry.getEmail(),base64Encoder.encodeToString(randomBytes));
            HashMap<String, String> map = new HashMap<>();
            map.put("access_token",base64Encoder.encodeToString(randomBytes));
            return ResponseEntity.ok(map);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("response","Incorrect password");
        return ResponseEntity.ok(map);
    }

    public boolean verifyToken(String email, String token){
        return tokenInMemoryRepo.verifyToken(email,token);
    }

}
