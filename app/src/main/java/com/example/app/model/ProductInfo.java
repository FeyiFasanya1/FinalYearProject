    package com.example.app.model;

    public class ProductInfo {

        String title;
        double price;

        int itemQuantity;
        String picUrl;

        String productId;


        public ProductInfo() {}

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getItemQuantity() {
            return itemQuantity;
        }

        public void setItemQuantity(int itemQuantity) {
            this.itemQuantity = itemQuantity;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }



    }
