    package com.example.app;

    public class User {


        private boolean isAdmin;
        private String userId;


        public User(boolean isAdmin, String userId) {
            this.isAdmin = isAdmin;
            this.userId = userId;
        }

        public User( ) {}

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }



    }
