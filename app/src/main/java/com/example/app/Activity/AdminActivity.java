    package com.example.app.Activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;

    import com.example.app.Adapter.OrdersAdapter;
    import com.example.app.MainActivity;
    import com.example.app.databinding.ActivityAdminBinding;
    import com.example.app.model.OrderInfo;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;

    public class AdminActivity extends AppCompatActivity {

        private ActivityAdminBinding binding;
        private List<OrderInfo> orders;
        private OrdersAdapter ordersAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityAdminBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            orders = new ArrayList<>();
            ordersAdapter = new OrdersAdapter(orders, orderId -> {
                Intent intent = new Intent(AdminActivity.this, OrderDetailActivity.class);
                intent.putExtra("ORDER_ID", orderId);
                startActivity(intent);
            });

            binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerViewOfficial.setAdapter(ordersAdapter);

            initOrders();
            bottomNavigation();

        }

        private void bottomNavigation() {
            binding.chartBtn2.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AdminChartActivity.class)));
            binding.profileBtn.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, MainActivity.class)));

        }


        private void initOrders() {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Orders");
            binding.progressBarOfficial.setVisibility(View.VISIBLE);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            orders.add(issue.getValue(OrderInfo.class));
                        }

                        Collections.sort(orders);
                        ordersAdapter.notifyDataSetChanged();
                        binding.progressBarOfficial.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBarOfficial.setVisibility(View.GONE);
                }
            });
        }
    }
