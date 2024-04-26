package com.example.app.Activity;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.app.Adapter.CartAdapter;
import com.example.app.Helper.ChangeNumberItemsListener;
import com.example.app.Helper.ManagmentCart;
import com.example.app.databinding.ActivityCartBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public class CartActivity extends BaseActivity {

    static final String PAYMENT_URL = "https://ask-lara.com/api/create-checkout-session?";

    static final String EMAIL_URL = "https://ask-lara.com/api/send-email-confirmation?";

    PaymentSheet paymentSheet;

    private String paymentClientSecret;

    PaymentSheet.CustomerConfiguration customerConfig;
    private static final String TAG = "CartActivity";

    ActivityCartBinding binding;
    private double tax;
    private double total;

    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        managmentCart = new ManagmentCart(this);

        calculatorCart();
        setVarialbe();
        initCartList();
    }


    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setVarialbe() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.checkOutBtn.setOnClickListener(v -> {
            launchStripe();
        });
    }

    private void launchStripe() {
        Fuel.INSTANCE.post(PAYMENT_URL + "totalPrice=" + (int) (total * 100), null)
                .responseString(new Handler<String>() {
                    @Override
                    public void success(String s) {
                        try {
                            final JSONObject result = new JSONObject(s);
                            customerConfig = new PaymentSheet.CustomerConfiguration(
                                    result.getString("customer"),
                                    result.getString("ephemeralKey")
                            );
                            paymentClientSecret = result.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));
                            presentPaymentSheet();
                        } catch (JSONException e) {
                            Log.d("Checkout", e.getMessage());
                        }
                    }

                    @Override
                    public void failure(@NonNull FuelError fuelError) { /* handle error */
                        Log.d("Checkout", fuelError.getMessage());
                    }
                });
    }

    private void presentPaymentSheet() {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Fashion Icon, Inc.")
                .customer(customerConfig)
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentClientSecret,
                configuration
        );
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Log.d(TAG, "Completed");
            Toast.makeText(this, "Payment is successful", Toast.LENGTH_SHORT).show();
            launchEmail();
        }
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText("€" + itemTotal);
        binding.taxTxt.setText("€" + tax);
        binding.deliveryTxt.setText("€" + delivery);
        binding.totalTxt.setText("€" + total);
    }

    private void launchEmail() {

        Fuel.INSTANCE.post(EMAIL_URL + "email=", null)
                .responseString(new Handler<String>() {
                    @Override
                    public void success(String s) {
                        Log.d(TAG, "Email sent successfully");
                        showToast("Email confirmation has been sent");
                    }

                    @Override
                    public void failure(@NonNull FuelError fuelError) {
                        Log.e(TAG, "Failed to send email: " + fuelError.getMessage());
                        showToast("Email confirmation cannot be sent");
                    }
                });

    }
        private void showToast(String message){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CartActivity.this, message, Toast.LENGTH_LONG).show();
                }
            });

        }
}

