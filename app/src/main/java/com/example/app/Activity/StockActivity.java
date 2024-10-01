    package com.example.app.Activity;
    
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    
    import com.example.app.Adapter.StockAdapter;
    import com.example.app.databinding.ActivityStockBinding;
    import com.example.app.Domain.ItemsDomain;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    
    import java.util.ArrayList;
    import java.util.List;
    
    public class StockActivity extends AppCompatActivity {
    
        private ActivityStockBinding binding;
        private List<ItemsDomain> itemsDomainList;
        private StockAdapter stockAdapter;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityStockBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
    
            itemsDomainList = new ArrayList<>();
            stockAdapter = new StockAdapter(itemsDomainList);
    
            binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerViewOfficial.setAdapter(stockAdapter);
    
            // Load the product list from the database
            loadProductData();
    
            // Back button listener to navigate back to AdminActivity
            binding.backBtn2.setOnClickListener(v -> {
                Intent intent = new Intent(StockActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
            });

        }
    
        private void loadProductData() {

            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Items");
    
            binding.progressBarOfficial.setVisibility(View.VISIBLE);
    
            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            ItemsDomain item = productSnapshot.getValue(ItemsDomain.class);
                            if (item != null) {
                                itemsDomainList.add(item);
                            }
                        }
                        Log.d("STOCK", String.valueOf(itemsDomainList.size()));
                        stockAdapter.notifyDataSetChanged();
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
