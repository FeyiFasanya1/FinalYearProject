    package com.example.app.Adapter;

    import android.content.Context;
    import android.graphics.Paint;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.app.Domain.QuantityDomain;
    import com.example.app.R;
    import com.example.app.databinding.ViewholderSizeBinding;

    import java.util.ArrayList;

    public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.Viewholder> {
        private ArrayList<String> items;

        private QuantityDomain quantityDomain;

        private Context context;
        private int selectedPosition = -1;
        private int lastSelectedPosition = -1;
        private SizeClickListener sizeClickListener;  // Listener for size selection

        // handle size selection
        public interface SizeClickListener {
            void onSizeClick(String size);
        }

        public SizeAdapter(ArrayList<String> items, QuantityDomain quantityDomain, SizeClickListener sizeClickListener) {
            this.items = items;
            this.quantityDomain = quantityDomain;
            this.sizeClickListener = sizeClickListener;
        }

        @NonNull
        @Override
        public SizeAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            ViewholderSizeBinding binding = ViewholderSizeBinding.inflate(LayoutInflater.from(context), parent, false);
            return new Viewholder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder holder, int position) {
            String size = items.get(position);
            int stockQuantity = quantityDomain.getQuantityForSize(size);
            
            holder.binding.sizeTxt.setText(items.get(position));

            if (stockQuantity > 0) {
                // Enable and style available sizes
                holder.binding.getRoot().setEnabled(true);
                holder.binding.sizeTxt.setPaintFlags(0); // Remove strike-through
                holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_unselected);
                holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.black));

                // Handle click for available sizes
                holder.binding.getRoot().setOnClickListener(v -> {
                    int lastSelectedPosition = selectedPosition;
                    selectedPosition = holder.getAdapterPosition();
                    notifyItemChanged(lastSelectedPosition);
                    notifyItemChanged(selectedPosition);

                    // Notify listener
                    if (sizeClickListener != null) {
                        sizeClickListener.onSizeClick(size);
                    }
                });
            } else {
                // Disable and style out-of-stock sizes
                holder.binding.getRoot().setEnabled(false);
                holder.binding.sizeTxt.setPaintFlags(holder.binding.sizeTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_unavailable);
                holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.grey));
            }

            // Highlight the selected size
            if (selectedPosition == holder.getAdapterPosition()) {
                holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_selected);
                holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.green));
            }
        }


        @Override
        public int getItemCount() {
            return items.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            ViewholderSizeBinding binding;

            public Viewholder(ViewholderSizeBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
