package com.example.app;

public class UserAuth {
    private static UserAuth instance;

    private User user;

    private UserAuth()
    {
    }

    public static synchronized UserAuth getInstance() {
        if (instance == null) {
            instance = new UserAuth();
        }
        return instance;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
