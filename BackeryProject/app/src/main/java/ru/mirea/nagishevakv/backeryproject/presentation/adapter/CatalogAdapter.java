package ru.mirea.nagishevakv.backeryproject.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mirea.nagishevakv.backeryproject.R;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onAddClick(Product product);
        void onMinusClick(Product product);
    }

    private final List<Product> products = new ArrayList<>();
    private Map<Product, Integer> cartMap;
    private final OnProductClickListener listener;

    public CatalogAdapter(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setProducts(List<Product> newProducts, Map<Product, Integer> cartMap) {
        this.products.clear();
        if (newProducts != null) {
            this.products.addAll(newProducts);
        }
        this.cartMap = cartMap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product, cartMap, listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivProduct;
        private final TextView tvName;
        private final TextView tvPrice;
        private final Button btnAddToCart;
        private final LinearLayout llQuantityControl;
        private final TextView tvQuantity;
        private final Button btnMinus;
        private final Button btnPlus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
            llQuantityControl = itemView.findViewById(R.id.ll_quantity_control);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnPlus = itemView.findViewById(R.id.btn_plus);
        }

        public void bind(Product product, Map<Product, Integer> cartMap, OnProductClickListener listener) {
            tvName.setText(product.getName());
            tvPrice.setText(String.format("%.2f ₽", product.getPrice()));

            int quantity = 0;
            if (cartMap != null) {
                for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
                    if (entry.getKey().getId() == product.getId()) {
                        quantity = entry.getValue();
                        break;
                    }
                }
            }

            if (quantity > 0) {
                btnAddToCart.setVisibility(View.GONE);
                llQuantityControl.setVisibility(View.VISIBLE);
                tvQuantity.setText(String.valueOf(quantity));
            } else {
                btnAddToCart.setVisibility(View.VISIBLE);
                llQuantityControl.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> listener.onProductClick(product));
            btnAddToCart.setOnClickListener(v -> listener.onAddClick(product));
            btnPlus.setOnClickListener(v -> listener.onAddClick(product));
            btnMinus.setOnClickListener(v -> listener.onMinusClick(product));
        }
    }
}