    package com.example.app.Adapter;

    import android.content.Context;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.example.app.databinding.ViewholderStockBinding;
    import com.example.app.Domain.ItemsDomain;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    import java.util.List;

    public class StockAdapter extends RecyclerView.Adapter<StockAdapter.Viewholder> {
        private List<ItemsDomain> itemsDomainList;
        private Context context;

        public StockAdapter(List<ItemsDomain> itemsDomainList) {
            this.itemsDomainList = itemsDomainList;
        }

        @NonNull
        @Override
        public StockAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            ViewholderStockBinding binding = ViewholderStockBinding.inflate(LayoutInflater.from(context), parent, false);
            return new Viewholder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull StockAdapter.Viewholder holder, int position) {
            ItemsDomain currentItem = itemsDomainList.get(position);

            holder.binding.itemTitle.setText(currentItem.getTitle());
            holder.binding.itemQuantityValue.setText(String.valueOf(currentItem.getQuantity()));
            holder.binding.itemPriceValue.setText("€" + String.valueOf(currentItem.getPrice()));

            // Set the EditText fields to current values
            holder.binding.itemQuantityValue.setText(String.valueOf(currentItem.getQuantity()));
            holder.binding.itemPriceValue.setText(String.valueOf(currentItem.getPrice()));

            // Load the first image from the picUrl list using Glide
            if (!currentItem.getPicUrl().isEmpty()) {
                Glide.with(context)
                        .load(currentItem.getPicUrl().get(0))
                        .into(holder.binding.orderImage);
            }

            // Update button click listener
            holder.binding.updateBtn.setOnClickListener(v -> {
                String newQuantityStr = holder.binding.itemQuantityValue.getText().toString();
                String newPriceStr = holder.binding.itemPriceValue.getText().toString();

                if (!newQuantityStr.isEmpty() && !newPriceStr.isEmpty()) {
                    int newQuantity = Integer.parseInt(newQuantityStr);
                    double newPrice = Double.parseDouble(newPriceStr);

                    // Update the item's quantity and price
                    updateItemInDatabase(currentItem.getProductId(), newQuantity, newPrice);
                }
            });
        }

        private void updateItemInDatabase(String productId, int newQuantity, double newPrice) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Items").child(productId);
            databaseReference.child("quantity").setValue(newQuantity);
            databaseReference.child("price").setValue(newPrice)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Optionally, you can notify the user or update the UI
                            Log.d("STOCK", "Item updated successfully");
                        } else {
                            Log.d("STOCK", "Failed to update item");
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return itemsDomainList.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            ViewholderStockBinding binding;

            public Viewholder(ViewholderStockBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
