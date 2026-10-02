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
import java.util.Map;

import ru.mirea.nagishevakv.backeryproject.R;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;

public class OrderBillAdapter extends RecyclerView.Adapter<OrderBillAdapter.BillViewHolder> {

    private final List<Map.Entry<Product, Integer>> items = new ArrayList<>();
    private int discountCategoryId = -1;
    private String discountKeyword = "";

    public void setItems(Map<Product, Integer> cartMap, int discountCatId, String keyword) {
        this.items.clear();
        this.discountCategoryId = discountCatId;
        this.discountKeyword = keyword;
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
        holder.bind(item, discountCategoryId, discountKeyword);
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

        public void bind(Map.Entry<Product, Integer> item, int discountCatId, String keyword) {
            Product product = item.getKey();
            int qty = item.getValue();
            
            boolean hasDiscount = (product.getCategoryId() == discountCatId);
            if (!hasDiscount && keyword != null && !keyword.isEmpty()) {
                String name = product.getName().toLowerCase();
                if (keyword.equals("HOT") && name.contains("капучино") && !name.contains("айс")) hasDiscount = true;
                if (keyword.equals("COLD") && (name.contains("айс") || name.contains("лимонад"))) hasDiscount = true;
                if (keyword.equals("мороженое") && name.contains("мороженое")) hasDiscount = true;
            }

            double price = hasDiscount ? product.getPrice() * 0.85 : product.getPrice();
            
            tvNamePrice.setText(String.format(Locale.getDefault(), "%s, %.2f ₽", product.getName(), price));
            tvQuantity.setText(String.format("%d шт.", qty));
        }
    }
}
