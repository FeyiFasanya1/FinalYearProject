package com.example.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.Adapter.SizeAdapter;
import com.example.app.Adapter.SliderAdapter;
import com.example.app.Domain.ItemsDomain;
import com.example.app.Domain.QuantityDomain;
import com.example.app.Domain.SliderItems;
import com.example.app.Fragment.DescriptionFragment;
import com.example.app.Fragment.ReviewFragment;
import com.example.app.Fragment.SoldFragment;
import com.example.app.Helper.ManagmentCart;
import com.example.app.databinding.ActivityDetailBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemsDomain object;
    private String selectedSize = "L";  // Default size

    private int numberOrder = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        getBundles();
        initBanners();
        initSize();
        setupViewPager();

    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("small");
        list.add("medium");
        list.add("large");

        QuantityDomain quantity = object.getQuantity();
        binding.recyclerSize.setAdapter(new SizeAdapter(list, quantity, size -> selectedSize = size));
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }
        binding.viewpageSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewpageSlider));
        binding.viewpageSlider.setClipToPadding(false);
        binding.viewpageSlider.setClipChildren(false);
        binding.viewpageSlider.setOffscreenPageLimit(3);
        binding.viewpageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void getBundles() {
        object = (ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("€" + object.getPrice());
        binding.ratingBar.setRating((float) object.getRating());
        binding.ratingTxt.setText(object.getRating() + " Rating");

        binding.addTocartBtn.setOnClickListener(v -> {
            if (selectedSize == null) {
                Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference("Items")
                    .child(object.getProductId()).child("quantity").child(selectedSize);
            Log.d("Item ", object.getProductId() );
            Log.d("Selected size ", selectedSize);


            itemRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    Log.d("Task", task.toString());
                    int currentStock = task.getResult().getValue(Integer.class);
                    if (currentStock > 0) {
                        object.setNumberinCart(numberOrder);
                        object.setSelectedSize(selectedSize); // Set selected size for the item
                        managmentCart.insertItem(object);
                        Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Size " + selectedSize + " is out of stock", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error checking stock", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Description Tab
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        Bundle descriptionBundle = new Bundle();
        descriptionBundle.putString("description", object.getDescription());
        descriptionFragment.setArguments(descriptionBundle);
        adapter.addFrag(descriptionFragment, "Descriptions");

        // Reviews Tab
        ReviewFragment reviewFragment = new ReviewFragment();
        Bundle reviewBundle = new Bundle();

        // Log the productId before passing it
        String productId = object.getProductId();
        Log.d("DetailActivity", "Passing productId to ReviewFragment: " + productId);

        reviewBundle.putString("productId", object.getProductId()); // Pass product ID to load reviews specific to this item
        reviewFragment.setArguments(reviewBundle);
        adapter.addFrag(reviewFragment, "Reviews");

        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}