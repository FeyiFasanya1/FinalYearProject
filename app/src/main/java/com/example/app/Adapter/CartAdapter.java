    package com.example.app.Adapter;

    import android.content.Context;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.load.resource.bitmap.CenterCrop;
    import com.bumptech.glide.request.RequestOptions;
    import com.example.app.Domain.ItemsDomain;
    import com.example.app.Helper.ChangeNumberItemsListener;
    import com.example.app.Helper.ManagmentCart;
    import com.example.app.databinding.ViewholderCartBinding;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    import java.util.ArrayList;

    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
        ArrayList<ItemsDomain> listItemSelected;
        ChangeNumberItemsListener changeNumberItemsListener;
        private ManagmentCart managmentCart;

        public CartAdapter(ArrayList<ItemsDomain> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
            this.listItemSelected = listItemSelected;
            this.changeNumberItemsListener = changeNumberItemsListener;
            managmentCart = new ManagmentCart(context);
        }

        @NonNull
        @Override
        public CartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new Viewholder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CartAdapter.Viewholder holder, int position) {
            ItemsDomain item = listItemSelected.get(position);

            holder.binding.titleTxt.setText(item.getTitle());
            holder.binding.feeEachItem.setText("€" + item.getPrice());
            holder.binding.totalEachItem.setText("€" + Math.round((item.getNumberinCart() * item.getPrice())));
            holder.binding.numberItemTxt.setText(String.valueOf(item.getNumberinCart()));
            holder.binding.sizeTxt.setText(item.getSelectedSize()); // Display selected size

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop());

            Glide.with(holder.itemView.getContext())
                    .load(item.getPicUrl().get(0))
                    .apply(requestOptions)
                    .into(holder.binding.pic);

            // Increment Quantity
            holder.binding.plusCartBtn.setOnClickListener(v -> {
                String sizeKey = item.getSelectedSize();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Items");
                Log.d("Size" , sizeKey);
                // Check stock for selected size
                database.child(item.getProductId()).child("quantity").child(sizeKey).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        int currentStock = task.getResult().getValue(Integer.class);

                        if (currentStock > item.getNumberinCart()) {
                            // Increment cart item
                            managmentCart.plusItem(listItemSelected, position, sizeKey, () -> {
                                notifyDataSetChanged();
                                changeNumberItemsListener.changed();
                            });
                        } else {
                            // Notify insufficient stock
                            Toast.makeText(holder.itemView.getContext(), "Insufficient stock for size " + sizeKey, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle Firebase query error
                        Toast.makeText(holder.itemView.getContext(), "Error checking stock for size " + sizeKey, Toast.LENGTH_SHORT).show();
                    }
                });
            });

            // Decrement Quantity
            holder.binding.minusCartBtn.setOnClickListener(v -> {
                if (item.getNumberinCart() > 1) {
                    // Decrement cart item
                    managmentCart.minusItem(listItemSelected, position, () -> {
                        notifyDataSetChanged();
                        changeNumberItemsListener.changed();
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return listItemSelected.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            ViewholderCartBinding binding;

            public Viewholder(ViewholderCartBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
