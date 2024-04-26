package com.example.app;

import retrofit2.Response;
import retrofit2.http.GET;

import java.io.IOException;

public interface ApiInterface {

    @GET("payment-fetch.cs")
    Response<PaymentDetailsResponse> fetchPaymentDetails() throws IOException;
}
