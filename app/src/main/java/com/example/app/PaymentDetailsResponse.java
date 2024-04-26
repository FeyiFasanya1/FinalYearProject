package com.example.app;

public class PaymentDetailsResponse {
    private String customer;
    private String ephemeralKey;
    private String paymentIntent;
    private String publishableKey;

    public PaymentDetailsResponse(String customer, String ephemeralKey, String paymentIntent, String publishableKey) {
        this.customer = customer;
        this.ephemeralKey = ephemeralKey;
        this.paymentIntent = paymentIntent;
        this.publishableKey = publishableKey;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEphemeralKey() {
        return ephemeralKey;
    }

    public void setEphemeralKey(String ephemeralKey) {
        this.ephemeralKey = ephemeralKey;
    }

    public String getPaymentIntent() {
        return paymentIntent;
    }

    public void setPaymentIntent(String paymentIntent) {
        this.paymentIntent = paymentIntent;
    }

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }
}
