    package com.example.app.Domain;

    import java.io.Serializable;
    import java.util.ArrayList;

    public class ItemsDomain implements Serializable {
        private String title;
        private String description;
        private ArrayList<String> picUrl;
        private double price;
        private double oldPrice;
        private int review;
        private double rating;
        private int NumberinCart;
        private String categoryId;
        private int quantity; // This represents the stock quantity
        private boolean isOutOfStock; //  field to indicate stock status

        private String productId;

        public ItemsDomain() {}

        public ItemsDomain(String title, String description, ArrayList<String> picUrl, double price, double oldPrice, int review, double rating, int NumberinCart, String categoryId, int quantity, String productId) {
            this.title = title;
            this.description = description;
            this.picUrl = picUrl;
            this.price = price;
            this.oldPrice = oldPrice;
            this.review = review;
            this.rating = rating;
            this.NumberinCart = NumberinCart;
            this.categoryId = categoryId;
            this.quantity = quantity;
            this.isOutOfStock = quantity <= 0; // Determine stock status on creation
            this.productId = productId;

        }
        //  check if an item can be added to the cart based on stock
        public boolean canAddToCart(int quantity) {
            return this.quantity >= quantity;
        }

        // Getters and Setters for all fields

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ArrayList<String> getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(ArrayList<String> picUrl) {
            this.picUrl = picUrl;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getOldPrice() {
            return oldPrice;
        }

        public void setOldPrice(double oldPrice) {
            this.oldPrice = oldPrice;
        }

        public int getReview() {
            return review;
        }

        public void setReview(int review) {
            this.review = review;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public int getNumberinCart() {
            return NumberinCart;
        }

        public void setNumberinCart(int NumberinCart) {
            this.NumberinCart = NumberinCart;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
            this.isOutOfStock = quantity <= 0; // Update stock status
        }

        public boolean isOutOfStock() {
            return isOutOfStock;
        }
        public void setOutOfStock(boolean b) {
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        @Override
        public String toString() {
            return "ItemsDomain{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", picUrl=" + picUrl +
                    ", price=" + price +
                    ", oldPrice=" + oldPrice +
                    ", review=" + review +
                    ", rating=" + rating +
                    ", NumberinCart=" + NumberinCart +
                    ", categoryId='" + categoryId + '\'' +
                    ", quantity=" + quantity +
                    ", isOutOfStock=" + isOutOfStock +
                    '}';
        }



    }
