package com.cmpt370_geocacheapp;

public class User {
    private int userID;
    private String password;
    private String username;

    public User(String username, String password, int userID) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public void changePassword(String currentPassword, String newPassword) {
        if (currentPassword.equals(this.password)) {
            this.password = newPassword;
        }
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getUserID() {
        return this.userID;
    }

    public static void main(String[] args) {
        //TODO: testing for user
    }
}
