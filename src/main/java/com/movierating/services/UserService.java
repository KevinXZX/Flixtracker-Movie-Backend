package com.movierating.services;

import com.movierating.AccessTokenInMemoryRepo;
import com.movierating.obj.UserEntry;
import com.movierating.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    /**
     * Encrypts a password with BCrypt encryption
     *
     * @param password plaintext password
     * @return hashed version of the password
     */
    public static String generateHash(String password) {
        return encoder.encode(password);
    }

    public boolean validPassword(String dbPassword, String incPassword) {
        return encoder.matches(incPassword, dbPassword);
    }

    /**
     * @param newUserEntry user to be registered
     * @return JSON of whether the registration was successful
     */
    public ResponseEntity<Object> register(UserEntry newUserEntry) {
        HashMap<String, String> map = new HashMap<>();
        if (userRepo.findByName(newUserEntry.getName()).size() != 0) {
            map.put("response", "Name already used");
            return ResponseEntity.status(409).body(map);
        } else if (userRepo.findByEmail(newUserEntry.getEmail()).size() != 0) {
            map.put("response", "Email already used");
            return ResponseEntity.status(409).body(map);
        }
        newUserEntry.setPassword(generateHash(newUserEntry.getPassword()));
        userRepo.save(newUserEntry);
        //TODO: Replace with JWT
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        tokenInMemoryRepo.addToken(newUserEntry.getEmail(), base64Encoder.encodeToString(randomBytes));
        //
        ResponseCookie authCookie = ResponseCookie.from("flix_auth_token", base64Encoder.encodeToString(randomBytes)) // key & value
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")  // sameSite
                .build();

        map.put("response", "User created");
        map.put("access_token", base64Encoder.encodeToString(randomBytes));
        map.put("user_id", String.valueOf(newUserEntry.getId()));
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .body(map);
    }

    /**
     * @param userEntry user who wishes to log in
     * @return JSON object of whether the user logged in successfully
     */
    public ResponseEntity<Object> login(UserEntry userEntry) {
        if (userRepo.findByEmail(userEntry.getEmail()).isEmpty()) {
            return ResponseEntity.status(404).body("Account does not exist");
        }
        UserEntry user = userRepo.findByEmail(userEntry.getEmail()).get(0);
        final String correctPassword = user.getPassword();
        if (encoder.matches(userEntry.getPassword(), correctPassword)) {
            byte[] randomBytes = new byte[64];
            secureRandom.nextBytes(randomBytes);
            tokenInMemoryRepo.addToken(userEntry.getEmail(), base64Encoder.encodeToString(randomBytes));
            ResponseCookie authCookie = ResponseCookie.from("flix_auth_token", base64Encoder.encodeToString(randomBytes)) // key & value
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Lax")  // sameSite
                    .build();
            HashMap<String, String> map = new HashMap<>();
            map.put("access_token", base64Encoder.encodeToString(randomBytes));
            map.put("user_id", String.valueOf(user.getId()));
            map.put("username", String.valueOf(user.getName()));
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                    .body(map);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("response", "Incorrect password");
        return ResponseEntity.ok(map);
    }

    /**
     * @param email email of the user
     * @param token access token
     * @return if the token is associated with that email
     */
    public boolean verifyToken(String email, String token) {
        return tokenInMemoryRepo.verifyToken(email, token);
    }

}
