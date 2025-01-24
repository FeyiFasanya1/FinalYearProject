        package com.example.app.Adapter;

        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.ViewGroup;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.example.app.Domain.ItemsDomain;
        import com.example.app.Domain.ReviewDomain;
        import com.example.app.databinding.ViewholderOrderDetailBinding;
        import com.example.app.model.ProductInfo;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.List;

        public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.Viewholder> {
            private List<ProductInfo> productInfoList;
            private Context context;

            public OrderDetailAdapter(List<ProductInfo> productInfoList) {
                this.productInfoList = productInfoList;
            }

            @NonNull
            @Override
            public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                context = parent.getContext();
                ViewholderOrderDetailBinding binding = ViewholderOrderDetailBinding.inflate(LayoutInflater.from(context), parent, false);
                return new Viewholder(binding);
            }

            @Override
            public void onBindViewHolder(@NonNull Viewholder holder, int position) {
                ProductInfo product = productInfoList.get(position);

                // Debugging: Log product info
                Log.d("BindViewHolder", "Product Title: " + product.getTitle());
                Log.d("BindViewHolder", "Product Pic URL: " + product.getPicUrl());
                Log.d("BindViewHolder", "Product ID: " + product.getProductId());

                // Bind product data to views
                holder.binding.orderTitle.setText(productInfoList.get(position).getTitle());
                holder.binding.itemQuantityValue.setText(String.valueOf(productInfoList.get(position).getItemQuantity()));
                holder.binding.itemPriceValue.setText('€' + String.valueOf(productInfoList.get(position).getPrice()));

                // Load image using Glide
                Glide.with(context)
                        .load(productInfoList.get(position).getPicUrl())
                        .into(holder.binding.orderImage);

                // Debugging: Log product info
                Log.d("BindViewHolder", "Product Title: " + product.getTitle());
                Log.d("BindViewHolder", "Product Pic URL: " + product.getPicUrl());
                Log.d("BindViewHolder", "Product ID: " + product.getProductId());

                // Set up click listener for saveReviewBtn
                holder.binding.saveReviewBtn.setOnClickListener(v -> {
                    String reviewText = holder.binding.orderReview.getText().toString();

                    if (reviewText.isEmpty()) {
                        // Handle empty review input
                        Toast.makeText(context, "Please enter a review before saving.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Save the review
                    saveReview(reviewText, product);
                });
            }
            private void saveReview(String reviewText, ProductInfo product) {
                DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Review");

                // Create a ReviewDomain object
                ReviewDomain review = new ReviewDomain();
                review.setReviewText(reviewText);
//                review.setName(itemsDomain.getTitle());
//                review.setPicUrl(itemsDomain.getPicUrl());
                  //review.setProductId(itemsDomain.getProductId());
                review.setProductId(product.getProductId());
                  // Set productId as an attribute
//                review.setItemId(productInfo.getId()); // Optional: if you also want to track itemId


                // Debugging: Log all values being sent to Firebase
                Log.d("SaveReview", "Review Text: " + reviewText);
                Log.d("SaveReview", "Product Name: " + product.getTitle());
                Log.d("SaveReview", "Product Pic URL: " + product.getPicUrl());
                Log.d("SaveReview", "Product ID: " + product.getProductId());

                // Push the review to the database
                reviewRef.push().setValue(review).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Review saved successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to save review.", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public int getItemCount() {
                return productInfoList.size();
            }

            public class Viewholder extends RecyclerView.ViewHolder {
                ViewholderOrderDetailBinding binding;

                public Viewholder(ViewholderOrderDetailBinding binding) {
                    super(binding.getRoot());
                    this.binding = binding;
                }
            }
        }
