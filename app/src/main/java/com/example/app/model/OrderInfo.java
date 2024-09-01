package com.example.app.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderInfo implements Comparable<OrderInfo> {
    String email;

    String title;
    double totalPrice;

    String OrderQuantity;

    String orderStatus;

    Long orderDate;

    String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Long orderDate) {
        this.orderDate = orderDate;
    }

    public String getForamattedOrderDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
        String formattedDate = formatter.format(orderDate);
        return formattedDate;
    }



    public String getOrderStatus() {
        return orderStatus;
    }



    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    List<ProductInfo> productInfoList;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrderQuantity() {
        return OrderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        OrderQuantity = orderQuantity;
    }


    public List<ProductInfo> getProductInfoList() {
        return productInfoList;
    }

    public void setProductInfoList(List<ProductInfo> productInfoList) {
        this.productInfoList = productInfoList;
    }


    @Override
    public int compareTo(OrderInfo o) {
        return o.orderDate.compareTo(this.orderDate);
    }
}


