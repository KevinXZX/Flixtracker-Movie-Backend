package com.example.movierating;

import com.sun.istack.NotNull;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AuthRequest {
    @NotNull
    private String email;



    @NotNull
    private String authToken;
    private Long id;

    /**
     * Constructs an authentication request
     * @param email user's email
     * @param authToken generated access token
     */
    public AuthRequest(String email,String authToken){
        this.email = email;
        this.authToken = authToken;
    }

    public AuthRequest() {

    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }
}
