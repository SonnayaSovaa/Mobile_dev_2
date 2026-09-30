package ru.mirea.nagishevakv.backeryproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.presentation.AuthActivity;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.CartAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.CatalogAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.OrderBillAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.viewmodel.BakeryViewModel;

public class MainActivity extends AppCompatActivity {

    private BakeryViewModel viewModel;
    private FrameLayout container;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(BakeryViewModel.class);
        container = findViewById(R.id.container);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        updateBottomNavigationVisibility();

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_catalog) {
                viewModel.navigateTo("CATALOG");
                return true;
            } else if (id == R.id.nav_cart) {
                if (viewModel.isAuthorized()) {
                    viewModel.navigateTo("CART");
                    return true;
                } else {
                    Toast.makeText(this, "Войдите, чтобы пользоваться корзиной", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (id == R.id.nav_account) {
                viewModel.navigateTo("ACCOUNT");
                return true;
            } else if (id == R.id.nav_about) {
                viewModel.navigateTo("ABOUT");
                return true;
            }
            return false;
        });

        viewModel.getCurrentScreen().observe(this, this::renderScreen);
        
        if (getIntent().getBooleanExtra("GOTO_CATALOG", false)) {
            viewModel.navigateTo("CATALOG");
            bottomNavigation.setSelectedItemId(R.id.nav_catalog);
        }
    }

    private void updateBottomNavigationVisibility() {
        Menu menu = bottomNavigation.getMenu();
        menu.findItem(R.id.nav_cart).setVisible(viewModel.isAuthorized());
    }

    private void renderScreen(String screen) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        switch (screen) {
            case "CATALOG":
                setupCatalogScreen(inflater);
                break;
            case "ACCOUNT":
                setupAccountScreen(inflater);
                break;
            case "DETAIL":
                setupDetailScreen(inflater);
                break;
            case "CART":
                setupCartScreen(inflater);
                break;
            case "ORDER":
                setupOrderScreen(inflater);
                break;
            case "ABOUT":
                setupAboutScreen(inflater);
                break;
            case "AUTH":
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                break;
        }
    }

    private void setupCatalogScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_catalog, container, false);
        RecyclerView rvCatalog = view.findViewById(R.id.rv_catalog);
        
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvCatalog.setLayoutManager(layoutManager);

        CatalogAdapter adapter = new CatalogAdapter(new CatalogAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                viewModel.selectProduct(product);
            }

            @Override
            public void onAddClick(Product product) {
                if (viewModel.isAuthorized()) {
                    viewModel.addToCart(product);
                } else {
                    Toast.makeText(MainActivity.this, "Чтобы совершать покупки необходимо авторизоваться", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMinusClick(Product product) {
                viewModel.removeFromCart(product);
            }
        });
        
        layoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup(2));
        rvCatalog.setAdapter(adapter);

        viewModel.getCategories().observe(this, categories -> {
            viewModel.getProducts().observe(this, products -> {
                viewModel.getCartItems().observe(this, cartMap -> {
                    adapter.setData(categories, products, cartMap);
                });
            });
        });

        container.addView(view);
    }

    private void setupAccountScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_account, container, false);
        
        LinearLayout layoutAuthorized = view.findViewById(R.id.layout_authorized);
        LinearLayout layoutGuest = view.findViewById(R.id.layout_guest);
        
        if (viewModel.isAuthorized()) {
            layoutAuthorized.setVisibility(View.VISIBLE);
            layoutGuest.setVisibility(View.GONE);
            
            TextView tvNickname = view.findViewById(R.id.tv_nickname);
            TextView tvEmail = view.findViewById(R.id.tv_email);
            TextView tvOrderCount = view.findViewById(R.id.tv_order_count);
            Button btnGoToCart = view.findViewById(R.id.btn_go_to_cart);
            Button btnLogout = view.findViewById(R.id.btn_logout);

            viewModel.getClientInfo().observe(this, user -> {
                if (user != null) {
                    tvNickname.setText(user.getNickname());
                    tvEmail.setText(user.getEmail());
                    tvOrderCount.setText(String.format(Locale.getDefault(), "Количество заказов: %d", user.getOrderCount()));
                }
            });

            btnGoToCart.setOnClickListener(v -> viewModel.navigateTo("CART"));
            btnLogout.setOnClickListener(v -> viewModel.logout());
        } else {
            layoutAuthorized.setVisibility(View.GONE);
            layoutGuest.setVisibility(View.VISIBLE);
            
            Button btnLoginAccount = view.findViewById(R.id.btn_login_account);
            btnLoginAccount.setOnClickListener(v -> {
                viewModel.logout(); // Clears guest info
            });
        }

        container.addView(view);
    }

    private void setupDetailScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_detail, container, false);
        TextView tvName = view.findViewById(R.id.tv_detail_name);
        TextView tvCategory = view.findViewById(R.id.tv_detail_category);
        TextView tvWeight = view.findViewById(R.id.tv_detail_weight);
        TextView tvPrice = view.findViewById(R.id.tv_detail_price);
        TextView tvDescription = view.findViewById(R.id.tv_detail_description);
        Button btnAdd = view.findViewById(R.id.btn_detail_add);
        Button btnBack = view.findViewById(R.id.btn_detail_back);

        viewModel.getSelectedProduct().observe(this, product -> {
            if (product != null) {
                tvName.setText(product.getName());
                
                viewModel.getCategories().observe(this, categories -> {
                    String categoryName = "Неизвестно";
                    if (categories != null) {
                        for (Category c : categories) {
                            if (c.getId() == product.getCategoryId()) {
                                categoryName = c.getName();
                                break;
                            }
                        }
                    }
                    tvCategory.setText(String.format(Locale.getDefault(), "Категория: %s", categoryName));
                });

                tvWeight.setText(String.format(Locale.getDefault(), "Вес/Объём: %.0f %s", product.getWeightOrVolume(), product.getUnit()));
                tvPrice.setText(String.format(Locale.getDefault(), "%.2f ₽", product.getPrice()));
                tvDescription.setText(product.getDescription());

                btnAdd.setOnClickListener(v -> {
                    if (viewModel.isAuthorized()) {
                        viewModel.addToCart(product);
                        Toast.makeText(this, "Добавлено в корзину!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Чтобы совершать покупки необходимо авторизоваться", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnBack.setOnClickListener(v -> viewModel.navigateTo("CATALOG"));
        container.addView(view);
    }

    private void setupCartScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_cart, container, false);
        TextView tvSummary = view.findViewById(R.id.tv_cart_summary);
        Button btnCheckoutTop = view.findViewById(R.id.btn_checkout_top);
        Button btnCheckoutBottom = view.findViewById(R.id.btn_checkout_bottom);
        RecyclerView rvCart = view.findViewById(R.id.rv_cart);

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter adapter = new CartAdapter(new CartAdapter.OnCartQuantityChangeListener() {
            @Override
            public void onAdd(Product product) {
                viewModel.addToCart(product);
            }

            @Override
            public void onMinus(Product product) {
                viewModel.removeFromCart(product);
            }
        });
        rvCart.setAdapter(adapter);

        viewModel.getCartItems().observe(this, cartMap -> {
            adapter.setCartItems(cartMap);

            int totalCount = 0;
            double totalPrice = 0.0;
            if (cartMap != null) {
                for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
                    totalCount += entry.getValue();
                    totalPrice += entry.getKey().getPrice() * entry.getValue();
                }
            }

            tvSummary.setText(String.format(Locale.getDefault(), "Товаров: %d | Итого: %.2f ₽", totalCount, totalPrice));

            int distinctCount = cartMap != null ? cartMap.size() : 0;
            btnCheckoutBottom.setVisibility(distinctCount > 6 ? View.VISIBLE : View.GONE);
        });

        View.OnClickListener checkoutListener = v -> viewModel.navigateTo("ORDER");
        btnCheckoutTop.setOnClickListener(checkoutListener);
        btnCheckoutBottom.setOnClickListener(checkoutListener);

        container.addView(view);
    }

    private void setupOrderScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_order, container, false);
        RecyclerView rvOrderItems = view.findViewById(R.id.rv_order_items);
        TextView tvOrderTotal = view.findViewById(R.id.tv_order_total);
        Button btnConfirm = view.findViewById(R.id.btn_confirm_order);

        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        OrderBillAdapter adapter = new OrderBillAdapter();
        rvOrderItems.setAdapter(adapter);

        viewModel.getCartItems().observe(this, cartMap -> {
            adapter.setItems(cartMap);
            double totalPrice = 0.0;
            if (cartMap != null) {
                for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
                    totalPrice += entry.getKey().getPrice() * entry.getValue();
                }
            }
            tvOrderTotal.setText(String.format(Locale.getDefault(), "Итого к оплате: %.2f ₽", totalPrice));
            
            double finalTotalPrice = totalPrice;
            int totalCount = cartMap != null ? cartMap.values().stream().mapToInt(Integer::intValue).sum() : 0;
            
            btnConfirm.setOnClickListener(v -> {
                if (totalCount == 0) {
                    Toast.makeText(this, "Корзина пуста", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.checkout(finalTotalPrice, totalCount);
                Toast.makeText(this, "Заказ оформлен!", Toast.LENGTH_SHORT).show();
                viewModel.clearCart();
                viewModel.navigateTo("ACCOUNT");
            });
        });

        container.addView(view);
    }

    private void setupAboutScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_about, container, false);
        Button btnToCatalog = view.findViewById(R.id.btn_about_to_catalog);
        btnToCatalog.setOnClickListener(v -> viewModel.navigateTo("CATALOG"));
        container.addView(view);
    }
}