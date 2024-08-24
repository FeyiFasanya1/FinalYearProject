package com.example.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.model.OrderInfo;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private List<OrderInfo> orderList;
    private Context context;

    public OrdersAdapter(List<OrderInfo> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_order_detail, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        OrderInfo order = orderList.get(position);
        holder.bind(order);  // Bind order data to the view
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {

        private ImageView orderImage;
        private TextView orderTitle, orderTotalPrice, orderQuantity;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.order_image);
            orderTitle = itemView.findViewById(R.id.order_title);
            orderQuantity = itemView.findViewById(R.id.order_quantity);
            orderTotalPrice = itemView.findViewById(R.id.order_price);
        }

        public void bind(OrderInfo order) {
            orderTitle.setText(order.getOrderStatus());
            orderTotalPrice.setText(String.format("$%.2f", order.getTotalPrice()));
            Glide.with(context).load(order.getEmail()).into(orderImage);
        }
    }
}
