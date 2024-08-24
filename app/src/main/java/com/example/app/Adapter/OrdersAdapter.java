package com.example.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app.databinding.ViewholderOrderListBinding;
import com.example.app.model.OrderInfo;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    public interface OrdersCallback {
        void onItemCLick(String orderId);
    }
    private List<OrderInfo> orderList;
    private Context context;

    private OrdersCallback callback;

    public OrdersAdapter(List<OrderInfo> orderList, OrdersCallback callback) {
        this.orderList = orderList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderOrderListBinding binding = ViewholderOrderListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrdersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        OrderInfo order = orderList.get(position);
        holder.binding.orderStatusValue.setText(order.getOrderStatus());
        holder.binding.orderDate.setText(order.getForamattedOrderDate());
        holder.binding.orderPriceValue.setText('€' + String.valueOf(order.getTotalPrice()));
        holder.binding.orderListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemCLick(order.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {

        ViewholderOrderListBinding binding;

        public OrdersViewHolder(ViewholderOrderListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
