        package com.example.app.Activity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.ImageView;

        import com.example.app.Fragment.ReviewFragment;
        import com.example.app.databinding.ActivityOrdersDetailBinding;
        import com.example.app.databinding.ViewholderOrderDetailBinding;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;

        import com.example.app.Adapter.OrderDetailAdapter;
        import com.example.app.R;
        import com.example.app.model.OrderInfo;
        import com.example.app.model.ProductInfo;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;

        public class OrderDetailActivity extends AppCompatActivity {

            private ActivityOrdersDetailBinding binding;
            private OrderDetailAdapter orderDetailAdapter;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                binding = ActivityOrdersDetailBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());

                Intent intent = getIntent();
                String orderId = intent.getStringExtra("ORDER_ID");

                binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(this));
                initOrderDetail(orderId);

                ImageView backBtn = findViewById(R.id.backBtn3);
                backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
            }

            private void initOrderDetail(String orderId) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
                binding.progressBarOffical.setVisibility(View.VISIBLE);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Log the raw data snapshot to check if productId is present
                            Log.d("FirebaseData", "Snapshot: " + snapshot.toString());


                            // Map the snapshot data to OrderInfo object
                            OrderInfo orderInfo = snapshot.getValue(OrderInfo.class);
                            if (orderInfo != null && orderInfo.getProductInfoList() != null) {

                                for (ProductInfo product : orderInfo.getProductInfoList()) {
                                    Log.d("ProductInfo", "Product ID: " + product.getProductId());
                                    Log.d("ProductInfo", "Product Title: " + product.getTitle());
                                }
                                orderDetailAdapter = new OrderDetailAdapter(orderInfo.getProductInfoList(), orderId);
                                binding.recyclerViewOfficial.setAdapter(orderDetailAdapter);
                            }
                            binding.progressBarOffical.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.progressBarOffical.setVisibility(View.GONE);
                    }
                });
            }
        }