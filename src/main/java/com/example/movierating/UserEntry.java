package com.example.movierating;

import com.sun.istack.NotNull;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserEntry {
    @NotNull
    private String name;
    @NotNull
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String password;
    public UserEntry(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserEntry() {

    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("name",this.name);
        object.put("email",this.email);
        object.put("password",this.password);
        return object.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
