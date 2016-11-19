package com.shawnhillyer.admin.randomancer;

/**
 * Created by Admin on 11/18/2016.
 */
public class User {
    private static User mInstance;

    private String username;

    public static User getInstance() {
        if (mInstance == null) {
            mInstance = new User();
        }
        return mInstance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void clearUsername() {
        this.username = null;
    }

    public String getUsername() {
        return this.username;
    }

    private User() {
    }
}
