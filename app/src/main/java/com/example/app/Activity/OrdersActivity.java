package com.example.app.Activity;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.app.Adapter.OrdersAdapter;
import com.example.app.model.OrderInfo;
import com.example.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.app.databinding.ActivityOrdersBinding;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private ActivityOrdersBinding binding;
    private List<OrderInfo> orders;
    private OrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orders = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(orders);

        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewOfficial.setAdapter(ordersAdapter);

        initOrders();
    }

    private void initOrders() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Orders");
        binding.progressBarOffical.setVisibility(View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        orders.add(issue.getValue(OrderInfo.class));
                    }
                    ordersAdapter.notifyDataSetChanged();
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
