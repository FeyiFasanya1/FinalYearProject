    package com.example.app.Activity;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import com.example.app.R;
    import com.example.app.databinding.ActivityChartBinding;
    import com.example.app.model.OrderInfo;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import org.eazegraph.lib.models.BarModel;
    import org.eazegraph.lib.charts.BarChart;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;

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
        }

        private void initMonthlyOrders() {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Orders");
//            binding.progressBarBanner.setVisibility(View.VISIBLE);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            orders.add(issue.getValue(OrderInfo.class));
                        }
                        Log.d("ORDERS", String.valueOf(orders.size()));

                        // Group orders by month + calculate totals
                        Map<String, Double> monthTotals = getMonthTotals(orders);
                        for(String key : monthTotals.keySet()){
                            Log.d("Month",key + ":" + monthTotals.get(key));
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

        private Map<String, Double> getMonthTotals(List<OrderInfo> orders) {
            Map<String, Double> monthTotals = new HashMap<>();
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

            for (OrderInfo order : orders) {
                // Converting orderDate from Long to Date
                String month = monthFormat.format(order.getOrderDate());

                // Update the total for the month
                double total = monthTotals.getOrDefault(month, 0.0);
                total += order.getTotalPrice();
                monthTotals.put(month, total);
            }

            return monthTotals;
        }

        private void addBarsToChart(BarChart barChart, Map<String, Double> monthTotals) {
            for (Map.Entry<String, Double> entry : monthTotals.entrySet()) {
                String month = entry.getKey();
                double total = entry.getValue();

                // Creating a BarModel + adding to chart
                BarModel barModel = new BarModel(month, (float) total, 0xFF123456);
                barChart.addBar(barModel);
            }

            barChart.startAnimation();
        }
    }
