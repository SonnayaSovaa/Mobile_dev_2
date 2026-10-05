package ru.mirea.nagishevakv.backeryproject.presentation.adapter;

import android.graphics.drawable.Drawable;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    
    private int discountCategoryId = -1;
    private String discountKeyword = "";

    public CatalogAdapter(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Category> categories, List<Product> products, Map<Product, Integer> cartMap, int discountCategoryId, String discountKeyword) {
        this.items.clear();
        this.cartMap = cartMap;
        this.discountCategoryId = discountCategoryId;
        this.discountKeyword = discountKeyword;
        
        if (categories != null && products != null) {
            for (Category category : categories) {
                List<Product> categoryProducts = products.stream()
                        .filter(p -> p.getCategoryId() == category.getId())
                        .collect(Collectors.toList());
                
                if (!categoryProducts.isEmpty()) {
                    items.add(category);
                    items.addAll(categoryProducts);
                }
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
            ((HeaderViewHolder) holder).bind((Category) items.get(position), discountCategoryId);
        } else {
            ((ProductViewHolder) holder).bind((Product) items.get(position), cartMap, discountCategoryId, discountKeyword, listener);
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
        private final TextView tvCategoryDiscount;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            tvCategoryDiscount = itemView.findViewById(R.id.tv_category_discount);
        }

        public void bind(Category category, int discountCatId) {
            tvCategoryName.setText(category.getName());
            tvCategoryDiscount.setVisibility(category.getId() == discountCatId ? View.VISIBLE : View.GONE);
        }
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivProduct;
        private final TextView tvName;
        private final TextView tvPrice;
        private final Button btnAddToCart;
        private final LinearLayout llQuantityControl;
        private final TextView tvQuantity;
        private final TextView btnMinus;
        private final TextView btnPlus;
        private final TextView tvProductDiscount;

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
            tvProductDiscount = itemView.findViewById(R.id.tv_product_discount);
        }

        public void bind(Product product, Map<Product, Integer> cartMap, int discountCatId, String keyword, OnProductClickListener listener) {
            tvName.setText(product.getName());
            
            String fileName = product.getImageUrl();
            if (fileName != null && !fileName.isEmpty()) {
                try (InputStream is = itemView.getContext().getAssets().open("images/products/" + fileName)) {
                    Drawable d = Drawable.createFromStream(is, null);
                    ivProduct.setImageDrawable(d);
                } catch (IOException e) {
                    ivProduct.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            } else {
                ivProduct.setImageResource(android.R.drawable.ic_menu_report_image);
            }
            
            boolean hasDiscount = (product.getCategoryId() == discountCatId);
            if (!hasDiscount && !keyword.isEmpty()) {
                String name = product.getName().toLowerCase();
                if (keyword.equals("HOT") && name.contains("капучино") && !name.contains("айс")) hasDiscount = true;
                if (keyword.equals("COLD") && (name.contains("айс") || name.contains("лимонад"))) hasDiscount = true;
                if (keyword.equals("мороженое") && name.contains("мороженое")) hasDiscount = true;
            }

            double displayPrice = product.getPrice();
            if (hasDiscount) {
                displayPrice = displayPrice * 0.85;
                tvProductDiscount.setVisibility(View.VISIBLE);
                tvPrice.setText(String.format(Locale.getDefault(), "%.2f ₽", displayPrice));
            } else {
                tvProductDiscount.setVisibility(View.GONE);
                tvPrice.setText(String.format(Locale.getDefault(), "%d ₽", product.getPrice()));
            }

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
                
                int color = itemView.getContext().getColor(R.color.dark_brown);
                btnMinus.setTextColor(color);
                btnPlus.setTextColor(color);
            } else {
                btnAddToCart.setVisibility(View.VISIBLE);
                llQuantityControl.setVisibility(View.GONE);
                
                btnMinus.setTextColor(itemView.getContext().getColor(R.color.white));
                btnPlus.setTextColor(itemView.getContext().getColor(R.color.white));
            }

            itemView.setOnClickListener(v -> listener.onProductClick(product));
            btnAddToCart.setOnClickListener(v -> listener.onAddClick(product));
            btnPlus.setOnClickListener(v -> listener.onAddClick(product));
            btnMinus.setOnClickListener(v -> listener.onMinusClick(product));
        }
    }
}
