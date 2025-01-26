    package com.example.app.Adapter;

    import android.content.Context;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.example.app.Domain.ReviewDomain;
    import com.example.app.databinding.ViewholderOrderDetailBinding;
    import com.example.app.model.ProductInfo;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.List;

    public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.Viewholder> {
        private final List<ProductInfo> productInfoList;
        private final String orderId; // To associate reviews with the correct order
        private Context context;

        public OrderDetailAdapter(List<ProductInfo> productInfoList, String orderId) {
            this.productInfoList = productInfoList;
            this.orderId = orderId;
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


            // Bind product data to views
            holder.binding.orderTitle.setText(product.getTitle());
            holder.binding.itemQuantityValue.setText(String.valueOf(product.getItemQuantity()));
            holder.binding.itemPriceValue.setText("€" + product.getPrice());

            // Load product image using Glide
            Glide.with(context)
                    .load(product.getPicUrl())
                    .into(holder.binding.orderImage);

            // Load existing reviews for the specific orderId and productId
            DatabaseReference reviewRef = FirebaseDatabase.getInstance()
                    .getReference("Review")
                    .child(orderId) // Use the orderId
                    .child(product.getProductId()); // Use the productId

            reviewRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    StringBuilder reviews = new StringBuilder();
                    for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                        String reviewText = reviewSnapshot.child("reviewText").getValue(String.class);
                        reviews.append("- ").append(reviewText).append("\n");
                    }

                    // Display reviews in the TextView
                    holder.binding.orderReview.setText(reviews.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("LoadReviews", "Failed to load reviews: " + error.getMessage());
                }
            });

            // Handle review saving
            holder.binding.saveReviewBtn.setOnClickListener(v -> {
                String reviewText = holder.binding.orderReview.getText().toString();

                if (reviewText.isEmpty()) {
                    Toast.makeText(context, "Please enter a review before saving.", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveReview(reviewText, product, orderId);
            });
        }

        private void saveReview(String reviewText, ProductInfo product, String orderId) {
            DatabaseReference reviewRef = FirebaseDatabase.getInstance()
                    .getReference("Review")
                    .child(product.getProductId()); // Associate review with the specific productId

            // Create a ReviewDomain object
            ReviewDomain review = new ReviewDomain();
            review.setReviewText(reviewText);
            review.setProductId(product.getProductId());
            review.setOrderId(orderId);


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

        public static class Viewholder extends RecyclerView.ViewHolder {
            ViewholderOrderDetailBinding binding;

            public Viewholder(ViewholderOrderDetailBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
