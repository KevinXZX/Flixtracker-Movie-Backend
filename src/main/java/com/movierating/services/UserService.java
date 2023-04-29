package com.movierating.services;

import com.movierating.helpers.JwtTokenUtil;
import com.movierating.obj.UserEntry;
import com.movierating.repo.UserRepo;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserRepo userRepo;
  static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  @Autowired private JwtTokenUtil jwtTokenUtil;

  /**
   * Encrypts a password with BCrypt encryption.
   *
   * @param password plaintext password
   * @return hashed version of the password
   */
  public static String generateHash(String password) {
    return encoder.encode(password);
  }

  public boolean matchesPassword(String dbPassword, String incPassword) {
    return encoder.matches(dbPassword, incPassword);
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
    UserEntry user = userRepo.findByEmail(newUserEntry.getEmail()).get(0);
    String token = jwtTokenUtil.createToken(user.getId());
    ResponseCookie authCookie =
        ResponseCookie.from("flix_auth_token", token) // key & value
            .httpOnly(true)
            .secure(true)
            .sameSite("Lax") // sameSite
            .build();
    map.put("username", String.valueOf(user.getName()));
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, authCookie.toString()).body(map);
  }

  /**
   * @param userEntry user who wishes to log in
   * @return JSON object of whether the user logged in successfully
   */
  public ResponseEntity<Object> login(UserEntry userEntry) {
    if (userRepo.findByEmail(userEntry.getEmail()).isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Account Details");
    }
    UserEntry user = userRepo.findByEmail(userEntry.getEmail()).get(0);
    final String correctPassword = user.getPassword();
    if (matchesPassword(userEntry.getPassword(), correctPassword)) {
      ResponseCookie authCookie =
          ResponseCookie.from("flix_auth_token", jwtTokenUtil.createToken(user.getId()))
              .httpOnly(true)
              .secure(true)
              .sameSite("Lax") // sameSite
              .build();
      HashMap<String, String> map = new HashMap<>();
      map.put("username", String.valueOf(user.getName()));
      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, authCookie.toString()).body(map);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Account Details");
  }

  public boolean userExists(int id) {
    return userRepo.findById(id).isPresent();
  }

  public UserEntry findUserByUsername(String username) {
    return userRepo.findByName(username).get(0);
  }
}
