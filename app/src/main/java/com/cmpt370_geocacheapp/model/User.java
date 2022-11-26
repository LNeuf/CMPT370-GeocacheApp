package com.cmpt370_geocacheapp.model;

public class User {
    private final int userID;
    private String password;
    private final String username;

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
        User user = new User("Joel", "Pollak356", 240);
        String username = "Joel";
        String password = "Pollak356";
        String newPassword = "Pollak999";
        long userID = 240;

        int totalTests = 0;
        int successfulTests = 0;
        if (!user.getUsername().equals(username)) {
            successfulTests--;
            System.out.println("Username not matching expected username");
        }
        totalTests++;
        successfulTests++;

        if (user.getUserID() != userID) {
            successfulTests--;
            System.out.println("UserID not matching expected value.");
        }
        totalTests++;
        successfulTests++;

        if (!user.getPassword().equals(password)) {
            successfulTests--;
            System.out.println("Password not matching expected password.");
        }
        totalTests++;
        successfulTests++;

        user.changePassword(password, newPassword);

        if (!user.getPassword().equals(newPassword)) {
            successfulTests--;
            System.out.println("new password not matching expected password: possibly not changed");
        }
        totalTests++;
        successfulTests++;

        System.out.println(successfulTests + " out of " + totalTests + " tests were completed.");
    }
}
