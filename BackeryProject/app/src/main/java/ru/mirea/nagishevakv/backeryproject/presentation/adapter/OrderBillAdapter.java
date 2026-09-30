package ru.mirea.nagishevakv.backeryproject.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mirea.nagishevakv.backeryproject.R;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;

public class OrderBillAdapter extends RecyclerView.Adapter<OrderBillAdapter.BillViewHolder> {

    private final List<Map.Entry<Product, Integer>> items = new ArrayList<>();

    public void setItems(Map<Product, Integer> cartMap) {
        this.items.clear();
        if (cartMap != null) {
            this.items.addAll(cartMap.entrySet());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Map.Entry<Product, Integer> item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class BillViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNamePrice;
        private final TextView tvQuantity;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamePrice = itemView.findViewById(R.id.tv_order_item_name);
            tvQuantity = itemView.findViewById(R.id.tv_order_item_quantity);
        }

        public void bind(Map.Entry<Product, Integer> item) {
            Product product = item.getKey();
            int qty = item.getValue();
            tvNamePrice.setText(String.format("%s, %.2f ₽", product.getName(), product.getPrice()));
            tvQuantity.setText(String.format("%d шт.", qty));
        }
    }
}