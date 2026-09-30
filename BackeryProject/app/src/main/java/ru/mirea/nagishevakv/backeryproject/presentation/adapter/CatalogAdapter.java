package ru.mirea.nagishevakv.backeryproject.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ru.mirea.nagishevakv.backeryproject.R;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;

public class CatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PRODUCT = 1;

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onAddClick(Product product);
        void onMinusClick(Product product);
    }

    private final List<Object> items = new ArrayList<>();
    private Map<Product, Integer> cartMap;
    private final OnProductClickListener listener;

    public CatalogAdapter(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Category> categories, List<Product> products, Map<Product, Integer> cartMap) {
        this.items.clear();
        this.cartMap = cartMap;
        if (categories != null && products != null) {
            for (Category category : categories) {
                items.add(category);
                List<Product> categoryProducts = products.stream()
                        .filter(p -> p.getCategoryId() == category.getId())
                        .collect(Collectors.toList());
                items.addAll(categoryProducts);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof Category ? TYPE_HEADER : TYPE_PRODUCT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((Category) items.get(position));
        } else {
            ((ProductViewHolder) holder).bind((Product) items.get(position), cartMap, listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup(int spanCount) {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return getItemViewType(position) == TYPE_HEADER ? spanCount : 1;
            }
        };
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategoryName;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
        }

        public void bind(Category category) {
            tvCategoryName.setText(category.getName());
        }
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