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

    public CartAdapter(OnCartQuantityChangeListener listener) {
        this.listener = listener;
    }

    public void setCartItems(Map<Product, Integer> newCartMap) {
        this.cartMap = newCartMap;
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
        holder.bind(product, cartMap, listener);
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
        }

        public void bind(Product product, Map<Product, Integer> cartMap, OnCartQuantityChangeListener listener) {
            tvName.setText(product.getName());
            tvPrice.setText(String.format("%d ₽", product.getPrice()));

            // Load image from assets/images/products/
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

            btnPlus.setOnClickListener(v -> listener.onAdd(product));
            btnMinus.setOnClickListener(v -> listener.onMinus(product));
        }
    }
}