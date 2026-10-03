package ru.mirea.nagishevakv.backeryproject.presentation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.io.InputStream;

import ru.mirea.nagishevakv.backeryproject.MainActivity;
import ru.mirea.nagishevakv.backeryproject.databinding.ActivityAuthBinding;
import ru.mirea.nagishevakv.backeryproject.presentation.viewmodel.BakeryViewModel;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    private BakeryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(BakeryViewModel.class);

        try (InputStream is = getAssets().open("images/logos/Big_logo.png")) {
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            binding.ivLogo.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите почту и пароль", Toast.LENGTH_SHORT).show();
                return;
            }
            
            viewModel.login(email, password).observe(this, success -> {
                if (success) {
                    navigateToMain();
                } else {
                    Toast.makeText(this, "Ошибка входа. Проверьте данные или интернет", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите данные для регистрации", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (password.length() < 6) {
                Toast.makeText(this, "Пароль должен быть не менее 6 символов", Toast.LENGTH_SHORT).show();
                return;
            }
            
            viewModel.register(email, password, "User").observe(this, success -> {
                if (success) {
                    Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                    navigateToMain();
                } else {
                    Toast.makeText(this, "Ошибка регистрации. Проверьте корректность Email или подключение", Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.btnGuest.setOnClickListener(v -> {
            viewModel.loginAsGuest();
            navigateToMain();
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        intent.putExtra("GOTO_CATALOG", true);
        startActivity(intent);
        finish();
    }
}