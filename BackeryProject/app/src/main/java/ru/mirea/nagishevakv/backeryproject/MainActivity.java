package ru.mirea.nagishevakv.backeryproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.presentation.AuthActivity;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.CartAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.CatalogAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.CategoryNavAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.OrderBillAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.OrdersHistoryAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.viewmodel.BakeryViewModel;

public class MainActivity extends AppCompatActivity {

    private BakeryViewModel viewModel;
    private FrameLayout container;
    private BottomNavigationView bottomNavigation;
    private final String[] cities = {"Выберите город", "Moscow", "London", "Paris", "Berlin", "Tokyo", "New York", "Dubai"};

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
            } else if (id == R.id.nav_orders) {
                if (viewModel.isAuthorized()) {
                    viewModel.navigateTo("ORDERS_HISTORY");
                    return true;
                } else {
                    Toast.makeText(this, "Войдите, чтобы посмотреть заказы", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (id == R.id.nav_weather) {
                viewModel.navigateTo("WEATHER");
                return true;
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
        menu.findItem(R.id.nav_orders).setVisible(viewModel.isAuthorized());
    }

    private void renderScreen(String screen) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        switch (screen) {
            case "CATALOG":
                setupCatalogScreen(inflater);
                break;
            case "WEATHER":
                setupWeatherScreen(inflater);
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
            case "ORDERS_HISTORY":
                setupOrdersHistoryScreen(inflater);
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

    private void syncCitySpinner(Spinner spinner) {
        String currentCity = viewModel.getSelectedCity().getValue();
        if (currentCity != null) {
            for (int i = 0; i < cities.length; i++) {
                if (cities[i].equalsIgnoreCase(currentCity)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupCatalogScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_catalog, container, false);
        RecyclerView rvCatalog = view.findViewById(R.id.rv_catalog);
        RecyclerView rvCategoryNav = view.findViewById(R.id.rv_categories_nav);
        EditText etSearch = view.findViewById(R.id.et_search);
        EditText etMaxPrice = view.findViewById(R.id.et_max_price);
        Spinner spinnerCities = view.findViewById(R.id.spinner_cities_catalog);
        Button btnSelectCity = view.findViewById(R.id.btn_select_city_catalog);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        spinnerCities.setAdapter(cityAdapter);
        syncCitySpinner(spinnerCities);

        btnSelectCity.setOnClickListener(v -> viewModel.fetchWeather(spinnerCities.getSelectedItem().toString()));

        etSearch.addTextChangedListener(new SimpleTextWatcher(s -> viewModel.setNameFilter(s)));
        etMaxPrice.addTextChangedListener(new SimpleTextWatcher(s -> {
            try {
                viewModel.setMaxPriceFilter(s.isEmpty() ? null : Double.parseDouble(s));
            } catch (NumberFormatException e) {
                viewModel.setMaxPriceFilter(null);
            }
        }));

        rvCategoryNav.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CategoryNavAdapter navAdapter = new CategoryNavAdapter(categoryId -> viewModel.setCategoryFilter(categoryId));
        rvCategoryNav.setAdapter(navAdapter);
        viewModel.getCategories().observe(this, navAdapter::setCategories);
        viewModel.getCategoryFilter().observe(this, navAdapter::setSelectedCategoryId);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvCatalog.setLayoutManager(layoutManager);
        CatalogAdapter adapter = new CatalogAdapter(new CatalogAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) { viewModel.selectProduct(product); }
            @Override
            public void onAddClick(Product product) {
                if (viewModel.isAuthorized()) viewModel.addToCart(product);
                else Toast.makeText(MainActivity.this, "Войдите для покупок", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onMinusClick(Product product) { viewModel.removeFromCart(product); }
        });
        layoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup(2));
        rvCatalog.setAdapter(adapter);

        viewModel.getCategories().observe(this, categories -> updateCatalogData(adapter));
        viewModel.getProducts().observe(this, products -> updateCatalogData(adapter));
        viewModel.getCartItems().observe(this, cart -> updateCatalogData(adapter));
        viewModel.getDiscountCategoryId().observe(this, id -> updateCatalogData(adapter));
        viewModel.getDiscountKeyword().observe(this, keyword -> updateCatalogData(adapter));

        container.addView(view);
    }

    private void updateCatalogData(CatalogAdapter adapter) {
        adapter.setData(
                viewModel.getCategories().getValue(),
                viewModel.getProducts().getValue(),
                viewModel.getCartItems().getValue(),
                viewModel.getDiscountCategoryId().getValue() != null ? viewModel.getDiscountCategoryId().getValue() : -1,
                viewModel.getDiscountKeyword().getValue() != null ? viewModel.getDiscountKeyword().getValue() : ""
        );
    }

    private void setupWeatherScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_weather, container, false);
        Spinner spinner = view.findViewById(R.id.spinner_cities);
        Button btn = view.findViewById(R.id.btn_get_weather);
        TextView tvTemp = view.findViewById(R.id.tv_temperature);
        TextView tvDesc = view.findViewById(R.id.tv_weather_description);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        spinner.setAdapter(adapter);
        syncCitySpinner(spinner);

        btn.setOnClickListener(v -> viewModel.fetchWeather(spinner.getSelectedItem().toString()));
        viewModel.getTemperature().observe(this, tvTemp::setText);
        viewModel.getWeatherDescription().observe(this, tvDesc::setText);

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
            Button btnOrdersHistory = new Button(this);
            btnOrdersHistory.setText("История заказов");
            btnOrdersHistory.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#5D4037")));
            btnOrdersHistory.setTextColor(android.graphics.Color.WHITE);
            ((LinearLayout)layoutAuthorized).addView(btnOrdersHistory, 2);

            viewModel.getClientInfo().observe(this, user -> {
                if (user != null) {
                    tvNickname.setText(user.getNickname());
                    tvEmail.setText(user.getEmail());
                }
            });
            viewModel.getOrders().observe(this, orders -> {
                tvOrderCount.setText(String.format(Locale.getDefault(), "Заказов: %d", orders.size()));
            });

            btnGoToCart.setOnClickListener(v -> viewModel.navigateTo("CART"));
            btnOrdersHistory.setOnClickListener(v -> viewModel.navigateTo("ORDERS_HISTORY"));
            btnLogout.setOnClickListener(v -> viewModel.logout());
        } else {
            layoutAuthorized.setVisibility(View.GONE);
            layoutGuest.setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_login_account).setOnClickListener(v -> viewModel.logout());
        }
        container.addView(view);
    }

    private void setupDetailScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_detail, container, false);
        ImageView ivPhoto = view.findViewById(R.id.iv_detail_photo);
        TextView tvName = view.findViewById(R.id.tv_detail_name);
        TextView tvCategory = view.findViewById(R.id.tv_detail_category);
        TextView tvWeight = view.findViewById(R.id.tv_detail_weight);
        TextView tvPrice = view.findViewById(R.id.tv_detail_price);
        TextView tvDescription = view.findViewById(R.id.tv_detail_description);
        Button btnAdd = view.findViewById(R.id.btn_detail_add);

        viewModel.getSelectedProduct().observe(this, product -> {
            if (product != null) {
                tvName.setText(product.getName());
                String fileName = product.getImageUrl();
                if (fileName != null && !fileName.isEmpty()) {
                    try (InputStream is = getAssets().open("images/products/" + fileName)) {
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        ivPhoto.setImageBitmap(bitmap);
                    } catch (IOException e) { ivPhoto.setImageResource(android.R.drawable.ic_menu_report_image); }
                }
                viewModel.getCategories().observe(this, categories -> {
                    String catName = categories.stream().filter(c -> c.getId() == product.getCategoryId()).findFirst().map(Category::getName).orElse("Неизвестно");
                    tvCategory.setText(String.format("Категория: %s", catName));
                });
                String unit = product.getUnit().toLowerCase();
                String label = (unit.equals("мл") || unit.equals("л")) ? "Объём" : "Вес";
                tvWeight.setText(String.format(Locale.getDefault(), "%s: %.0f %s", label, product.getWeightOrVolume(), product.getUnit()));
                tvPrice.setText(String.format(Locale.getDefault(), "%d ₽", product.getPrice()));
                tvDescription.setText(product.getDescription());
                btnAdd.setOnClickListener(v -> {
                    if (viewModel.isAuthorized()) { viewModel.addToCart(product); Toast.makeText(this, "Добавлено!", Toast.LENGTH_SHORT).show(); }
                    else Toast.makeText(this, "Войдите для покупок", Toast.LENGTH_SHORT).show();
                });
            }
        });
        view.findViewById(R.id.btn_detail_back).setOnClickListener(v -> viewModel.navigateTo("CATALOG"));
        container.addView(view);
    }

    private void setupCartScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_cart, container, false);
        TextView tvSummary = view.findViewById(R.id.tv_cart_summary);
        RecyclerView rvCart = view.findViewById(R.id.rv_cart);
        Spinner spinnerCities = view.findViewById(R.id.spinner_cities_cart);
        Button btnSelectCity = view.findViewById(R.id.btn_select_city_cart);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        spinnerCities.setAdapter(cityAdapter);
        syncCitySpinner(spinnerCities);
        btnSelectCity.setOnClickListener(v -> viewModel.fetchWeather(spinnerCities.getSelectedItem().toString()));

        rvCart.setLayoutManager(new GridLayoutManager(this, 2));
        CartAdapter adapter = new CartAdapter(new CartAdapter.OnCartQuantityChangeListener() {
            @Override public void onAdd(Product product) { viewModel.addToCart(product); }
            @Override public void onMinus(Product product) { viewModel.removeFromCart(product); }
        });
        rvCart.setAdapter(adapter);

        viewModel.getCartItems().observe(this, cartMap -> updateCartSummary(adapter, tvSummary, cartMap));
        viewModel.getDiscountCategoryId().observe(this, id -> updateCartSummary(adapter, tvSummary, viewModel.getCartItems().getValue()));
        viewModel.getDiscountKeyword().observe(this, keyword -> updateCartSummary(adapter, tvSummary, viewModel.getCartItems().getValue()));

        View.OnClickListener checkoutListener = v -> viewModel.navigateTo("ORDER");
        view.findViewById(R.id.btn_checkout_top).setOnClickListener(checkoutListener);
        view.findViewById(R.id.btn_checkout_bottom).setOnClickListener(checkoutListener);
        container.addView(view);
    }

    private void updateCartSummary(CartAdapter adapter, TextView tvSummary, Map<Product, Integer> cartMap) {
        int discountCatId = viewModel.getDiscountCategoryId().getValue() != null ? viewModel.getDiscountCategoryId().getValue() : -1;
        String keyword = viewModel.getDiscountKeyword().getValue() != null ? viewModel.getDiscountKeyword().getValue() : "";
        adapter.setCartItems(cartMap, discountCatId, keyword);

        double total = 0;
        int count = 0;
        if (cartMap != null) {
            for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
                Product p = entry.getKey();
                int qty = entry.getValue();
                count += qty;
                boolean hasDiscount = (p.getCategoryId() == discountCatId);
                if (!hasDiscount && !keyword.isEmpty()) {
                    String name = p.getName().toLowerCase();
                    if (keyword.equals("HOT") && name.contains("капучино") && !name.contains("айс")) hasDiscount = true;
                    if (keyword.equals("COLD") && (name.contains("айс") || name.contains("лимонад"))) hasDiscount = true;
                    if (keyword.equals("мороженое") && name.contains("мороженое")) hasDiscount = true;
                }
                double price = hasDiscount ? p.getPrice() * 0.85 : p.getPrice();
                total += price * qty;
            }
        }
        tvSummary.setText(String.format(Locale.getDefault(), "Товаров: %d | Итого: %.2f ₽", count, total));
    }

    private void setupOrderScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_order, container, false);
        RecyclerView rvItems = view.findViewById(R.id.rv_order_items);
        TextView tvTotal = view.findViewById(R.id.tv_order_total);
        Spinner spinner = view.findViewById(R.id.spinner_cities_order);
        Button btnSelectCity = view.findViewById(R.id.btn_select_city_order);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        spinner.setAdapter(cityAdapter);
        syncCitySpinner(spinner);
        btnSelectCity.setOnClickListener(v -> viewModel.fetchWeather(spinner.getSelectedItem().toString()));

        rvItems.setLayoutManager(new LinearLayoutManager(this));
        OrderBillAdapter adapter = new OrderBillAdapter();
        rvItems.setAdapter(adapter);

        viewModel.getCartItems().observe(this, cartMap -> updateOrderSummary(adapter, tvTotal, cartMap, view));
        viewModel.getDiscountCategoryId().observe(this, id -> updateOrderSummary(adapter, tvTotal, viewModel.getCartItems().getValue(), view));
        viewModel.getDiscountKeyword().observe(this, keyword -> updateOrderSummary(adapter, tvTotal, viewModel.getCartItems().getValue(), view));

        container.addView(view);
    }

    private void updateOrderSummary(OrderBillAdapter adapter, TextView tvTotal, Map<Product, Integer> cartMap, View view) {
        if (cartMap == null) return;
        int discountCatId = viewModel.getDiscountCategoryId().getValue() != null ? viewModel.getDiscountCategoryId().getValue() : -1;
        String keyword = viewModel.getDiscountKeyword().getValue() != null ? viewModel.getDiscountKeyword().getValue() : "";
        adapter.setItems(cartMap, discountCatId, keyword);

        double total = 0;
        for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
            Product p = entry.getKey();
            boolean hasDiscount = (p.getCategoryId() == discountCatId);
            if (!hasDiscount && !keyword.isEmpty()) {
                String name = p.getName().toLowerCase();
                if (keyword.equals("HOT") && name.contains("капучино") && !name.contains("айс")) hasDiscount = true;
                if (keyword.equals("COLD") && (name.contains("айс") || name.contains("лимонад"))) hasDiscount = true;
                if (keyword.equals("мороженое") && name.contains("мороженое")) hasDiscount = true;
            }
            total += (hasDiscount ? p.getPrice() * 0.85 : p.getPrice()) * entry.getValue();
        }
        tvTotal.setText(String.format(Locale.getDefault(), "Итого: %.2f ₽", total));

        double finalTotal = total;
        view.findViewById(R.id.btn_confirm_order).setOnClickListener(v -> {
            if (cartMap.isEmpty()) return;
            String desc = cartMap.entrySet().stream().map(e -> e.getKey().getName() + " x" + e.getValue()).collect(Collectors.joining(", "));
            viewModel.checkout(finalTotal, cartMap.values().stream().mapToInt(i -> i).sum(), desc, viewModel.getSelectedCity().getValue());
            Toast.makeText(this, "Заказ оформлен!", Toast.LENGTH_SHORT).show();
            viewModel.clearCart();
            viewModel.navigateTo("ORDERS_HISTORY");
        });
    }

    private void setupOrdersHistoryScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_orders_history, container, false);
        RecyclerView rv = view.findViewById(R.id.rv_orders_history);
        rv.setLayoutManager(new LinearLayoutManager(this));
        OrdersHistoryAdapter adapter = new OrdersHistoryAdapter();
        rv.setAdapter(adapter);
        viewModel.getOrders().observe(this, adapter::setOrders);
        container.addView(view);
    }

    private void setupAboutScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_about, container, false);
        view.findViewById(R.id.btn_about_to_catalog).setOnClickListener(v -> viewModel.navigateTo("CATALOG"));
        container.addView(view);
    }

    private static class SimpleTextWatcher implements TextWatcher {
        private final java.util.function.Consumer<String> consumer;
        public SimpleTextWatcher(java.util.function.Consumer<String> consumer) { this.consumer = consumer; }
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) { consumer.accept(s.toString()); }
        @Override public void afterTextChanged(Editable s) {}
    }
}