package ru.mirea.nagishevakv.backeryproject.presentation.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.mirea.nagishevakv.backeryproject.R;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartQuantityChangeListener {
        void onAdd(Product product);
        void onMinus(Product product);
    }

    private final List<Product> cartProducts = new ArrayList<>();
    private Map<Product, Integer> cartMap;
    private final OnCartQuantityChangeListener listener;
    private int discountCategoryId = -1;
    private String discountKeyword = "";

    public CartAdapter(OnCartQuantityChangeListener listener) {
        this.listener = listener;
    }

    public void setCartItems(Map<Product, Integer> newCartMap, int discountCatId, String keyword) {
        this.cartMap = newCartMap;
        this.discountCategoryId = discountCatId;
        this.discountKeyword = keyword;
        this.cartProducts.clear();
        if (newCartMap != null) {
            this.cartProducts.addAll(newCartMap.keySet());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartProducts.get(position);
        holder.bind(product, cartMap, discountCategoryId, discountKeyword, listener);
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivProduct;
        private final TextView tvName;
        private final TextView tvPrice;
        private final Button btnAddToCart;
        private final LinearLayout llQuantityControl;
        private final TextView tvQuantity;
        private final Button btnMinus;
        private final Button btnPlus;
        private final TextView tvProductDiscount;

        public CartViewHolder(@NonNull View itemView) {
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

        public void bind(Product product, Map<Product, Integer> cartMap, int discountCatId, String keyword, OnCartQuantityChangeListener listener) {
            tvName.setText(product.getName());
            
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

            String fileName = product.getImageUrl();
            if (fileName != null && !fileName.isEmpty()) {
                try (InputStream is = itemView.getContext().getAssets().open("images/products/" + fileName)) {
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if (bitmap != null) {
                        ivProduct.setImageBitmap(bitmap);
                    } else {
                        ivProduct.setImageResource(android.R.drawable.ic_menu_report_image);
                    }
                } catch (IOException e) {
                    ivProduct.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            } else {
                ivProduct.setImageResource(android.R.drawable.ic_menu_report_image);
            }

            int quantity = cartMap != null && cartMap.get(product) != null ? cartMap.get(product) : 0;

            btnAddToCart.setVisibility(View.GONE);
            llQuantityControl.setVisibility(View.VISIBLE);
            tvQuantity.setText(String.valueOf(quantity));

            // Set text color for + and - buttons in the cart
            int color = itemView.getContext().getColor(R.color.dark_brown);
            btnMinus.setTextColor(color);
            btnPlus.setTextColor(color);

            btnPlus.setOnClickListener(v -> listener.onAdd(product));
            btnMinus.setOnClickListener(v -> listener.onMinus(product));
        }
    }
}