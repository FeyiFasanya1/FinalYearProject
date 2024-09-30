    package com.example.app.Adapter;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.example.app.databinding.ViewholderStockBinding;
    import com.example.app.Domain.ItemsDomain;  // Import the ItemsDomain class

    import java.util.List;

    public class StockAdapter extends RecyclerView.Adapter<StockAdapter.Viewholder> {
        private List<ItemsDomain> itemsDomainList;  // Update list type to ItemsDomain
        private Context context;

        public StockAdapter(List<ItemsDomain> itemsDomainList) {  // Update constructor to use ItemsDomain
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
            ItemsDomain currentItem = itemsDomainList.get(position);  // Use ItemsDomain object

            holder.binding.itemTitle.setText(currentItem.getTitle());
            holder.binding.itemQuantityValue.setText(String.valueOf(currentItem.getQuantity()));
            holder.binding.itemPriceValue.setText('€' + String.valueOf(currentItem.getPrice()));

            // Load the first image from the picUrl list using Glide
            if (!currentItem.getPicUrl().isEmpty()) {
                Glide.with(context)
                        .load(currentItem.getPicUrl().get(0))  // Load the first image URL from the ArrayList
                        .into(holder.binding.orderImage);
            }
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
