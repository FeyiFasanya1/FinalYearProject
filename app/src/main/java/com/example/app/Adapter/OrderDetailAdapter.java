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
        private List <ProductInfo> productInfoList;

        private Context context;

        public OrderDetailAdapter(List<ProductInfo> productInfoList) {
            this.productInfoList = productInfoList;
        }


        @NonNull
        @Override
        public OrderDetailAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            ViewholderOrderDetailBinding binding = ViewholderOrderDetailBinding.inflate(LayoutInflater.from(context), parent, false);
            return new Viewholder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderDetailAdapter.Viewholder holder, int position) {
            holder.binding.orderTitle.setText(productInfoList.get(position).getTitle());
            holder.binding.orderDate.setText(productInfoList.get(position).getTitle());
            holder.binding.orderQuantity.setText(productInfoList.get(position).getTitle());
            holder.binding.itemPrice.setText(productInfoList.get(position).getTitle());

            Glide.with(context)
                    .load(productInfoList.get(position).getPicUrl())
                    .into(holder.binding.orderImage);

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
