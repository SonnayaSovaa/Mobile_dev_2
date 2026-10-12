package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import java.util.Map;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.repository.ProductRepository;

public class GetCartUseCase {
    private final ProductRepository repository;

    public GetCartUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    public LiveData<Map<Product, Integer>> execute() {
        return repository.getCartItems();
    }
}