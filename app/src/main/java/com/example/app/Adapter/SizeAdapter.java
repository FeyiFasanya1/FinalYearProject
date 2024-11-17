    package com.example.app.Adapter;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.app.R;
    import com.example.app.databinding.ViewholderSizeBinding;

    import java.util.ArrayList;

    public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.Viewholder> {
        private ArrayList<String> items;
        private Context context;
        private int selectedPosition = -1;
        private int lastSelectedPosition = -1;
        private SizeClickListener sizeClickListener;  // Listener for size selection

        // handle size selection
        public interface SizeClickListener {
            void onSizeClick(String size);
        }

        public SizeAdapter(ArrayList<String> items, SizeClickListener sizeClickListener) {
            this.items = items;
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
        public void onBindViewHolder(@NonNull SizeAdapter.Viewholder holder, int position) {
            holder.binding.sizeTxt.setText(items.get(position));

            // Handle size selection on click
            holder.binding.getRoot().setOnClickListener(v -> {
                lastSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);

                // Notify the listener of the selected size
                if (sizeClickListener != null) {
                    sizeClickListener.onSizeClick(items.get(position)); // Pass the selected size back
                }
            });

            // Highlight the selected size
            if (selectedPosition == holder.getAdapterPosition()) {
                holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_selected);
                holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.green));
            } else {
                holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_unselected);
                holder.binding.sizeTxt.setTextColor(context.getResources().getColor(R.color.black));
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
