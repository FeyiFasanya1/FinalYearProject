    package com.example.app.Adapter;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.example.app.Domain.CategoryDomain;
    import com.example.app.databinding.ViewholderCategoryBinding;

    import java.util.ArrayList;

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
        private ArrayList<CategoryDomain> items;
        private Context context;
        private CategoryClickListener listener;

        // Define the interface
        public interface CategoryClickListener {
            void onCategoryClick(String categoryId);
        }

        // Modify the constructor to accept the listener
        public CategoryAdapter(ArrayList<CategoryDomain> items, CategoryClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public CategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
            return new Viewholder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryAdapter.Viewholder holder, int position) {
            CategoryDomain currentCategory = items.get(position);
            holder.binding.title.setText(currentCategory.getTitle());

            Glide.with(context)
                    .load(currentCategory.getPicUrl())
                    .into(holder.binding.pic);

            // Set the onClickListener to notify the listener when a category is clicked
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(currentCategory.getCategoryId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            ViewholderCategoryBinding binding;

            public Viewholder(ViewholderCategoryBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
