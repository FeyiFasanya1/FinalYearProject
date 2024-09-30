    package com.example.app.Activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.Editable;
    import android.text.TextWatcher;
    import android.util.Log;
    import android.view.View;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.viewpager2.widget.CompositePageTransformer;
    import androidx.viewpager2.widget.MarginPageTransformer;

    import com.example.app.Adapter.CategoryAdapter;
    import com.example.app.Adapter.PopularAdapter;
    import com.example.app.Adapter.SliderAdapter;
    import com.example.app.Domain.CategoryDomain;
    import com.example.app.Domain.ItemsDomain;
    import com.example.app.Domain.SliderItems;
    import com.example.app.databinding.ActivityHomeBinding;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;

    public class HomeActivity extends BaseActivity implements CategoryAdapter.CategoryClickListener {
        private ActivityHomeBinding binding;
        private PopularAdapter popularAdapter;
        private ArrayList<ItemsDomain> items;
        private DatabaseReference database; // Firebase database reference

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityHomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Initialize Firebase database reference
            database = FirebaseDatabase.getInstance().getReference();

            initBanner();
            initCategory();
            initPopular(); // Load all items initially
            initSearchBar();
            bottomNavigation();
        }

        private void bottomNavigation() {
            binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CartActivity.class)));
            binding.ordersBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, OrdersActivity.class)));
            binding.chartBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ChartActivity.class)));
            binding.profileBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
            binding.textView4.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, HomeActivity.class)));
        }

        private void initPopular() {
            DatabaseReference myref = database.child("Items");
            binding.progressBarPopular.setVisibility(View.VISIBLE);
            items = new ArrayList<>();

            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            ItemsDomain item = issue.getValue(ItemsDomain.class);
                            Log.d("PRODUCT ID", item.getProductId());
                            if (item != null) {
                                // Check stock quantity
                                if (item.getQuantity() <= 0) {
                                    item.setOutOfStock(true); // ItemsDomain
                                } else {
                                    item.setOutOfStock(false);
                                }
                                items.add(item);
                            }
                        }
                        if (!items.isEmpty()) {
                            binding.recyclerviewPopular.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
                            popularAdapter = new PopularAdapter(items);
                            binding.recyclerviewPopular.setAdapter(popularAdapter);
                        }
                        binding.progressBarPopular.setVisibility(View.GONE);
                    } else {
                        binding.progressBarPopular.setVisibility(View.GONE);
                        // Optionally handle no items found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBarPopular.setVisibility(View.GONE);
                    // Handle error appropriately
                }
            });
        }

        private void initSearchBar() {
            binding.searchBar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("ORDER SEARCH","ORDER SEARCH IF NOT WORKING");
                    if (popularAdapter != null) {
                        Log.d("ORDER SEARCH","ORDER SEARCH");
                        popularAdapter.filter(s.toString());
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        private void initCategory() {
            DatabaseReference myref = database.child("Category");
            binding.progressBarOffical.setVisibility(View.VISIBLE);
            ArrayList<CategoryDomain> categories = new ArrayList<>();

            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            CategoryDomain category = issue.getValue(CategoryDomain.class);
                            if (category != null) {
                                categories.add(category);
                            }
                        }
                        if (!categories.isEmpty()) {
                            binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(HomeActivity.this,
                                    LinearLayoutManager.HORIZONTAL, false));
                            binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(categories, HomeActivity.this));
                        }
                        binding.progressBarOffical.setVisibility(View.GONE);
                    } else {
                        binding.progressBarOffical.setVisibility(View.GONE);
                        // Optionally handle no categories found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBarOffical.setVisibility(View.GONE);
                    // Handle error appropriately
                }
            });
        }

        private void initBanner() {
            DatabaseReference myRef = database.child("Banner");
            binding.progressBarBanner.setVisibility(View.VISIBLE);
            ArrayList<SliderItems> banners = new ArrayList<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            SliderItems banner = issue.getValue(SliderItems.class);
                            if (banner != null) {
                                banners.add(banner);
                            }
                        }
                        setupBanners(banners);
                        binding.progressBarBanner.setVisibility(View.GONE);
                    } else {
                        binding.progressBarBanner.setVisibility(View.GONE);
                        // Optionally handle no banners found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBarBanner.setVisibility(View.GONE);
                    // Handle error appropriately
                }
            });
        }

        private void setupBanners(ArrayList<SliderItems> banners) {
            binding.viewpagerSlider.setAdapter(new SliderAdapter(banners, binding.viewpagerSlider));
            binding.viewpagerSlider.setClipToPadding(false);
            binding.viewpagerSlider.setClipChildren(false);
            binding.viewpagerSlider.setOffscreenPageLimit(3);
            binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));

            binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
        }

        @Override
        public void onCategoryClick(String categoryId) {
            Log.d("CategoryClick", "Selected Category ID: " + categoryId);
            // Fetch and display items matching the categoryId
            DatabaseReference itemsRef = database.child("Items");
            binding.progressBarPopular.setVisibility(View.VISIBLE);
            ArrayList<ItemsDomain> filteredItems = new ArrayList<>();

            itemsRef.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.d("FirebaseData", "Items found for category: " + categoryId);
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            Log.d("FirebaseData", "Item: " + issue.getValue());
                            ItemsDomain item = issue.getValue(ItemsDomain.class);
                            if (item != null) {
                                // Check stock quantity
                                if (item.getQuantity() <= 0) {
                                    item.setOutOfStock(true);
                                } else {
                                    item.setOutOfStock(false);
                                }
                                filteredItems.add(item);
                            }
                        }
                        updatePopularRecyclerView(filteredItems);
                    } else {
                        Log.d("FirebaseData", "No items found for category: " + categoryId);
                        // Optionally handle no items found
                        updatePopularRecyclerView(new ArrayList<>());
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBarPopular.setVisibility(View.GONE);
                    // Handle error appropriately
                }
            });
        }

        private void updatePopularRecyclerView(ArrayList<ItemsDomain> newItems) {
            if (popularAdapter != null) {
                popularAdapter.updateList(newItems);
            } else {
                binding.recyclerviewPopular.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
                popularAdapter = new PopularAdapter(newItems);
                binding.recyclerviewPopular.setAdapter(popularAdapter);
            }
        }
    }
