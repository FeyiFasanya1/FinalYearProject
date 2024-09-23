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
    import com.example.app.MainActivity;
    import com.example.app.databinding.ActivityHomeBinding;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;

    public class HomeActivity extends BaseActivity {
        private ActivityHomeBinding binding;
        private PopularAdapter popularAdapter;
        private ArrayList<ItemsDomain> items;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityHomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());


            initBanner();
            initCategory();
            initPopular();
            initSearchBar();
            bottomNavigation();

        }


        private void bottomNavigation() {
            binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CartActivity.class)));
            binding.ordersBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, OrdersActivity.class)));
            binding.chartBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ChartActivity.class)));
            binding.profileBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));



        }

        private void initPopular() {
            DatabaseReference myref=database.getReference("Items");
            binding.progressBarPopular.setVisibility(View.VISIBLE);
            ArrayList<ItemsDomain> items=new ArrayList<>();

            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            items.add(issue.getValue(ItemsDomain.class));
                        }
                        if(!items.isEmpty()){
                            binding.recyclerviewPopular.setLayoutManager(new GridLayoutManager(HomeActivity.this,2));
                            popularAdapter = new PopularAdapter(items);
                            binding.recyclerviewPopular.setAdapter(popularAdapter);
                        }
                        binding.progressBarPopular.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
            DatabaseReference myref=database.getReference("Category");
            binding.progressBarOffical.setVisibility(View.VISIBLE);
            ArrayList<CategoryDomain> items=new ArrayList<>();
            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            items.add(issue.getValue(CategoryDomain.class));
                        }
                        if(!items.isEmpty()){
                            binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(HomeActivity.this,
                                    LinearLayoutManager.HORIZONTAL,false));
                            binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));

                        }
                        binding.progressBarOffical.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void initBanner() {
            DatabaseReference myRef = database.getReference("Banner");
            binding.progressBarBanner.setVisibility(View.VISIBLE);
            ArrayList<SliderItems> items = new ArrayList<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            items.add(issue.getValue(SliderItems.class));
                        }
                        banners(items);
                        binding.progressBarBanner.setVisibility(View.GONE);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void banners(ArrayList<SliderItems> items) {

            binding.viewpagerSlider.setAdapter(new SliderAdapter(items, binding.viewpagerSlider));
            binding.viewpagerSlider.setClipToPadding(false);
            binding.viewpagerSlider.setClipChildren(false);
            binding.viewpagerSlider.setOffscreenPageLimit(3);
            binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));

            binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
        }
    }