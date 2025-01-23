        package com.example.app.Adapter;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.example.app.databinding.ViewholderOrderDetailBinding;
        import com.example.app.model.ProductInfo;

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
                // Bind product data to views
                holder.binding.orderTitle.setText(productInfoList.get(position).getTitle());
                holder.binding.itemQuantityValue.setText(String.valueOf(productInfoList.get(position).getItemQuantity()));
                holder.binding.itemPriceValue.setText('€' + String.valueOf(productInfoList.get(position).getPrice()));

                // Load image using Glide
                Glide.with(context)
                        .load(productInfoList.get(position).getPicUrl())
                        .into(holder.binding.orderImage);

                // Set up click listener for saveReviewBtn
                holder.binding.saveReviewBtn.setOnClickListener(v -> {
                    String reviewText = holder.binding.orderReview.getText().toString();

                    if (reviewText.isEmpty()) {
                        // Handle empty review input
                        return;
                    }

                    // Save the review
                    saveReview(reviewText);
                });
            }

            @Override
            public int getItemCount() {
                return productInfoList.size();
            }

            private void saveReview(String reviewText) {
                // Implementation for saving review (you can add functionality here)
            }

            public class Viewholder extends RecyclerView.ViewHolder {
                ViewholderOrderDetailBinding binding;

                public Viewholder(ViewholderOrderDetailBinding binding) {
                    super(binding.getRoot());
                    this.binding = binding;
                }
            }
        }
