    package com.example.app.Activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import com.example.app.R;
    import com.example.app.databinding.ActivityChartBinding;
    import com.example.app.model.OrderInfo;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import org.eazegraph.lib.charts.BarChart;
    import org.eazegraph.lib.models.BarModel;

    import java.text.SimpleDateFormat;
    import java.time.Month;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;
    import java.util.Set;

    public class ChartActivity extends AppCompatActivity {
        private ActivityChartBinding binding;
        private List<OrderInfo> orders;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityChartBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            orders = new ArrayList<>();

            initMonthlyOrders();

            binding.backBtn4.setOnClickListener(v -> {
                // Start HomeActivity when back button is clicked
                Intent intent = new Intent(ChartActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            });
        }

        private void initMonthlyOrders() {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Orders");
            binding.progressBarBanner.setVisibility(View.VISIBLE);

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user ID

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            OrderInfo order = issue.getValue(OrderInfo.class);
                            if (order != null && order.getUserId() != null && order.getUserId().equals(userId)) {
                                orders.add(order); // Only add orders that belong to the signed-in user
                            }
                        }
                        Log.d("ORDERS", String.valueOf(orders.size()));

                        // Group orders by month + calculate totals
                        Map<Integer, Double> monthTotals = getMonthTotals(orders);
                        for (Integer key : monthTotals.keySet()) {
                            Log.d("Month", key + ":" + monthTotals.get(key));
                        }

                        BarChart barChart = findViewById(R.id.barChart);
                        addBarsToChart(barChart, monthTotals);

                        binding.progressBarBanner.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            });
        }

        private Map<Integer, Double> getMonthTotals(List<OrderInfo> orders) {
            Map<Integer, Double> monthTotals = new HashMap<>();
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

            for (OrderInfo order : orders) {
                // Converting orderDate from Long to Date
                int month = new Date(order.getOrderDate()).getMonth() + 1;

                // Update the total for the month
                double total = monthTotals.getOrDefault(month, 0.0);
                total += order.getTotalPrice();
                monthTotals.put(month, total);
            }

            return monthTotals;
        }

        private void addBarsToChart(BarChart barChart, Map<Integer, Double> monthTotals) {
            Set<Integer> keySet = monthTotals.keySet();

            // Step 2: Convert keyset to a List
            List<Integer> keyList = new ArrayList<>(keySet);

            // Step 3: Sort the list
            Collections.sort(keyList);
            for (Integer entry : keyList) {
                Month month = Month.of(entry);

                // Get the abbreviation of the month
                String monthAbbreviation = month.name().substring(0, 3).toUpperCase();
                double total = monthTotals.get(entry);

                Log.d("Month", "number: " + entry + "month: " + monthAbbreviation + "total: " + total);

                // Creating a ValueLinePoint and adding to series
                barChart.addBar(new BarModel(monthAbbreviation, (float) total, 0xFF123456));
            }

            barChart.startAnimation();
        }
    }
