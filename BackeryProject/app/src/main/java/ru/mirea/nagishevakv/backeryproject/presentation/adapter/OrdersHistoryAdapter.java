package ru.mirea.nagishevakv.backeryproject.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import ru.mirea.nagishevakv.backeryproject.R;
import ru.mirea.nagishevakv.backeryproject.domain.model.Order;

public class OrdersHistoryAdapter extends RecyclerView.Adapter<OrdersHistoryAdapter.OrderViewHolder> {

    private final List<Order> orders = new ArrayList<>();

    public void setOrders(List<Order> newOrders) {
        this.orders.clear();
        if (newOrders != null) {
            this.orders.addAll(newOrders);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvOrderId, tvOrderDate, tvOrderCity, tvOrderItems, tvOrderTotal, tvOrderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderCity = itemView.findViewById(R.id.tv_order_city);
            tvOrderItems = itemView.findViewById(R.id.tv_order_items);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
        }

        public void bind(Order order) {
            tvOrderId.setText(String.format(Locale.getDefault(), "Заказ №%d", order.getId()));
            tvOrderDate.setText(order.getDate());
            tvOrderCity.setText(String.format("Город: %s", order.getCity()));
            tvOrderItems.setText(order.getItemsDescription());
            tvOrderTotal.setText(String.format(Locale.getDefault(), "Итого: %.2f ₽", order.getCost()));
            tvOrderStatus.setText(order.getStatus());
            
            if ("Активен".equals(order.getStatus())) {
                tvOrderStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_orange_dark));
            } else {
                tvOrderStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
            }
        }
    }
}
