package ru.mirea.nagishevakv.backeryproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.CartAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.CatalogAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.adapter.OrderBillAdapter;
import ru.mirea.nagishevakv.backeryproject.presentation.viewmodel.BakeryViewModel;

public class MainActivity extends AppCompatActivity {

    private BakeryViewModel viewModel;
    private FrameLayout container;
    
    private View navCatalog, navCart, navAccount, navAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(BakeryViewModel.class);
        container = findViewById(R.id.container);

        navCatalog = findViewById(R.id.nav_catalog);
        navCart = findViewById(R.id.nav_cart);
        navAccount = findViewById(R.id.nav_account);
        navAbout = findViewById(R.id.nav_about);

        navCatalog.setOnClickListener(v -> viewModel.navigateTo("CATALOG"));
        navCart.setOnClickListener(v -> viewModel.navigateTo("CART"));
        navAccount.setOnClickListener(v -> viewModel.navigateTo("ACCOUNT"));
        navAbout.setOnClickListener(v -> viewModel.navigateTo("ABOUT"));

        viewModel.getCurrentScreen().observe(this, this::renderScreen);
    }

    private void renderScreen(String screen) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        switch (screen) {
            case "AUTH":
                setupAuthScreen(inflater);
                break;
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
        }
    }

    private void setupAuthScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_auth, container, false);
        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etPassword = view.findViewById(R.id.et_password);
        EditText etNickname = view.findViewById(R.id.et_nickname);
        Button btnLogin = view.findViewById(R.id.btn_login);
        Button btnRegister = view.findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            viewModel.login(email, password).observe(this, success -> {
                if (success) {
                    Toast.makeText(this, "Успешный вход!", Toast.LENGTH_SHORT).show();
                    viewModel.navigateTo("CATALOG");
                } else {
                    Toast.makeText(this, "Ошибка входа. Проверьте почту и пароль.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String nickname = etNickname.getText().toString().trim();
            viewModel.register(email, password, nickname).observe(this, success -> {
                if (success) {
                    Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                    viewModel.navigateTo("CATALOG");
                } else {
                    Toast.makeText(this, "Заполните все поля (пароль от 6 символов)", Toast.LENGTH_SHORT).show();
                }
            });
        });

        container.addView(view);
    }

    private void setupCatalogScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_catalog, container, false);
        RecyclerView rvCatalog = view.findViewById(R.id.rv_catalog);
        rvCatalog.setLayoutManager(new GridLayoutManager(this, 2));

        CatalogAdapter adapter = new CatalogAdapter(new CatalogAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                viewModel.selectProduct(product);
            }

            @Override
            public void onAddClick(Product product) {
                viewModel.addToCart(product);
            }

            @Override
            public void onMinusClick(Product product) {
                viewModel.removeFromCart(product);
            }
        });

        rvCatalog.setAdapter(adapter);

        viewModel.getCartItems().observe(this, cartMap -> {
            viewModel.getProducts().observe(this, products -> adapter.setProducts(products, cartMap));
        });

        container.addView(view);
    }

    private void setupAccountScreen(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.screen_account, container, false);
        TextView tvNickname = view.findViewById(R.id.tv_nickname);
        TextView tvEmail = view.findViewById(R.id.tv_email);
        TextView tvOrderCount = view.findViewById(R.id.tv_order_count);
        Button btnGoToCart = view.findViewById(R.id.btn_go_to_cart);

        viewModel.getClientInfo().observe(this, user -> {
            if (user != null) {
                tvNickname.setText(user.getNickname());
                tvEmail.setText(user.getEmail());
                tvOrderCount.setText(String.format(Locale.getDefault(), "Количество заказов: %d", user.getOrderCount()));
            }
        });

        btnGoToCart.setOnClickListener(v -> viewModel.navigateTo("CART"));
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
                tvCategory.setText(String.format(Locale.getDefault(), "Категория ID: %d", product.getCategoryId()));
                tvWeight.setText(String.format(Locale.getDefault(), "Вес/Объём: %.0f %s", product.getWeightOrVolume(), product.getUnit()));
                tvPrice.setText(String.format(Locale.getDefault(), "%.2f ₽", product.getPrice()));
                tvDescription.setText(product.getDescription());

                btnAdd.setOnClickListener(v -> {
                    viewModel.addToCart(product);
                    Toast.makeText(this, "Добавлено в корзину!", Toast.LENGTH_SHORT).show();
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

            tvSummary.setText(String.format(Locale.getDefault(), "Товаров в корзине: %d | Итого: %.2f ₽", totalCount, totalPrice));

            if (cartMap != null && cartMap.size() > 6) {
                btnCheckoutBottom.setVisibility(View.VISIBLE);
            } else {
                btnCheckoutBottom.setVisibility(View.GONE);
            }
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

        final int[] totalCountArr = {0};
        final double[] totalPriceArr = {0.0};

        viewModel.getCartItems().observe(this, cartMap -> {
            adapter.setItems(cartMap);
            int totalCount = 0;
            double totalPrice = 0.0;
            if (cartMap != null) {
                for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
                    totalCount += entry.getValue();
                    totalPrice += entry.getKey().getPrice() * entry.getValue();
                }
            }
            totalCountArr[0] = totalCount;
            totalPriceArr[0] = totalPrice;
            tvOrderTotal.setText(String.format(Locale.getDefault(), "Итого к оплате: %.2f ₽", totalPrice));
        });

        btnConfirm.setOnClickListener(v -> {
            if (totalCountArr[0] == 0) {
                Toast.makeText(this, "Ваша корзина пуста!", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.checkout(totalPriceArr[0], totalCountArr[0]);
            Toast.makeText(this, "Заказ успешно оформлен!", Toast.LENGTH_SHORT).show();
            viewModel.navigateTo("ACCOUNT");
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