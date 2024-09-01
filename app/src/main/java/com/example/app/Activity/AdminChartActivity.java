    package com.example.app.Activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import com.example.app.R;
    import com.example.app.databinding.ActivityAdminChartBinding;
    import com.example.app.model.OrderInfo;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import org.eazegraph.lib.charts.ValueLineChart;
    import org.eazegraph.lib.models.ValueLinePoint;
    import org.eazegraph.lib.models.ValueLineSeries;

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

    public class AdminChartActivity extends AppCompatActivity {
        private ActivityAdminChartBinding binding;
        private List<OrderInfo> orders;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityAdminChartBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            orders = new ArrayList<>();

            initMonthlyOrders();

            binding.backBtn4.setOnClickListener(v -> {
                // Start HomeActivity when back button is clicked
                Intent intent = new Intent(AdminChartActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();  // Finish current activity to remove it from the back stack
            });
        }

        private void initMonthlyOrders() {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Orders");
            binding.progressBarBanner.setVisibility(View.VISIBLE);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            orders.add(issue.getValue(OrderInfo.class));
                        }

                        Collections.sort(orders);


                        Log.d("ORDERS", String.valueOf(orders.size()));

                        // Group orders by month + calculate totals
                        Map<Integer, Double> monthTotals = getMonthTotals(orders);
                        for (Integer key : monthTotals.keySet()) {
                            Log.d("Month", key + ":" + monthTotals.get(key));
                        }

                        ValueLineChart valueLineChart = findViewById(R.id.cubiclinechart);
                        addPointsToChart(valueLineChart, monthTotals);

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
                int month = new Date(order.getOrderDate()).getMonth();

                // Update the total for the month
                double total = monthTotals.getOrDefault(month, 0.0);
                total += order.getTotalPrice();
                monthTotals.put(month, total);
            }

            return monthTotals;
        }

        private void addPointsToChart(ValueLineChart valueLineChart, Map<Integer, Double> monthTotals) {
            ValueLineSeries series = new ValueLineSeries();
            series.setColor(0xFF56B7F1);

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

                // Creating a ValueLinePoint and adding to series
                series.addPoint(new ValueLinePoint(monthAbbreviation, (float) total));
            }

            valueLineChart.addSeries(series);
            valueLineChart.startAnimation();
        }
    }
