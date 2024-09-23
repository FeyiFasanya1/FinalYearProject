    package com.example.app;

    public class User {

        private boolean isAdmin;
        private String userId;
        private String email;
        private String phoneNumber;
        private String address;

        // Constructor
        public User(boolean isAdmin, String userId, String email, String phoneNumber, String address) {
            this.isAdmin = isAdmin;
            this.userId = userId;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.address = address;
        }


        public User() {}

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

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
