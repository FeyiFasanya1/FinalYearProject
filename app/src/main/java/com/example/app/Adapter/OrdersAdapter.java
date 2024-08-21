package com.example.app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.model.OrderInfo;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private List<OrderInfo> orderList;

    public OrdersAdapter(List<OrderInfo> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_orders, parent, false);
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

    class OrdersViewHolder extends RecyclerView.ViewHolder {

        private TextView orderEmail, orderStatus, orderDate, orderTotalPrice;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            orderEmail = itemView.findViewById(R.id.order_email);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderDate = itemView.findViewById(R.id.order_date);
            orderTotalPrice = itemView.findViewById(R.id.order_total_price);
        }

        public void bind(OrderInfo order) {
            orderEmail.setText(order.getEmail());
            orderStatus.setText(order.getOrderStatus());
            orderDate.setText(String.valueOf(order.getOrderDate()));  // Convert to a readable date format if needed
            orderTotalPrice.setText(String.valueOf(order.getTotalPrice()));
        }
    }
}
