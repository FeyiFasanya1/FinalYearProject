    package com.example.app.Adapter;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Spinner;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.app.R;
    import com.example.app.databinding.ViewholderOrderListBinding;
    import com.example.app.model.OrderInfo;

    import java.util.List;

    public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

        public interface OrdersCallback {
            void onItemCLick(String orderId);
            void onOrderUpdated(OrderInfo order);
        }

        private List<OrderInfo> orderList;
        private OrdersCallback callback;
        private boolean isAdmin;

        public OrdersAdapter(List<OrderInfo> orderList, OrdersCallback callback, boolean isAdmin) {
            this.orderList = orderList;
            this.callback = callback;
            this.isAdmin = isAdmin;
        }

        @NonNull
        @Override
        public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewholderOrderListBinding binding = ViewholderOrderListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new OrdersViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
            OrderInfo order = orderList.get(position);

            if (isAdmin) {
                holder.binding.orderStatusSpinner.setVisibility(View.VISIBLE);
                holder.binding.orderStatusValue.setVisibility(View.GONE);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(holder.itemView.getContext(),
                R.array.order_status_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.binding.orderStatusSpinner.setAdapter(adapter);
                holder.binding.orderStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String orderStatus = "";

                        switch (position) {
                            case 0:
                                orderStatus = "ORDER PROCESSED";
                                break;
                            case 1:
                                orderStatus = "ORDER SHIPPED";
                                break;
                            case 2:
                                orderStatus = "ORDER DELIVERED";
                                break;
                        }

                        order.setOrderStatus(orderStatus);
                        callback.onOrderUpdated(order);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                switch (order.getOrderStatus()) {
                    case "ORDER PROCESSED":
                        holder.binding.orderStatusSpinner.setSelection(0);
                        break;
                    case "ORDER SHIPPED":
                        holder.binding.orderStatusSpinner.setSelection(1);
                        break;
                    case "ORDER DELIVERED":
                        holder.binding.orderStatusSpinner.setSelection(2);
                        break;
                }
            } else {
                holder.binding.orderStatusSpinner.setVisibility(View.GONE);
                holder.binding.orderStatusValue.setVisibility(View.VISIBLE);

                holder.binding.orderStatusValue.setText(order.getOrderStatus());
                
            }

            holder.binding.orderDate.setText(order.getForamattedOrderDate());
            holder.binding.orderPriceValue.setText('€' + String.valueOf(order.getTotalPrice()));

            holder.binding.orderListItem.setOnClickListener(v -> callback.onItemCLick(order.getId()));
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        public static class OrdersViewHolder extends RecyclerView.ViewHolder {

            ViewholderOrderListBinding binding;

            public OrdersViewHolder(ViewholderOrderListBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
