package com.example.furnitureorderingapp;

public class TestUser {
    private final String username;
    private final String password;
    private final String fullName;
    private final int id;
    public TestUser(int id, String username, String password, String fullName){
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }
    public int getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
}

