package com.example.noli.sphinx;

/**
 * Created by User on 30-Mar-17.
 */

public class User {
    private String Username;
    private String Email;



    public User(){

    }

    public User(String username, String email) {
        Username = username;
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
