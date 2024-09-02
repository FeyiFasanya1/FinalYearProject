    package com.example.app.Activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import com.example.app.Adapter.OrdersAdapter;
    import com.example.app.UserAuth;
    import com.example.app.model.OrderInfo;
    import com.google.firebase.auth.FirebaseAuth;
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


            // Determine if the user is an admin
            boolean isAdmin = UserAuth.getInstance().getUser().isAdmin();

            orders = new ArrayList<>();
            ordersAdapter = new OrdersAdapter(orders, new OrdersAdapter.OrdersCallback() {
                @Override
                public void onItemCLick(String orderId) {
                    Intent intent = new Intent(OrdersActivity.this, OrderDetailActivity.class);
                    intent.putExtra("ORDER_ID", orderId);

                    startActivity(intent);
                }

                @Override
                public void onOrderUpdated(OrderInfo order) {
                    // Update the order in firebase
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Orders/" + order.getId());
                    myRef.setValue(order);
                }
            }, isAdmin);

            binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerViewOfficial.setAdapter(ordersAdapter);

            initOrders();

            binding.backBtn2.setOnClickListener(v -> {
                getOnBackPressedDispatcher().onBackPressed();
            });
        }
        private void initOrders() {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Orders");

            binding.progressBarOfficial.setVisibility(View.VISIBLE);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            OrderInfo order = issue.getValue(OrderInfo.class);
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (order.getUserId() != null && order.getUserId().equals(userId)) {
                                orders.add(order);
                            }
                        }
                        Log.d("ORDERS", String.valueOf(orders.size()));

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
