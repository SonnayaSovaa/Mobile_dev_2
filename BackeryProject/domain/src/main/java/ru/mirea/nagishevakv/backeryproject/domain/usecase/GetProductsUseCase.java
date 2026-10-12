package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.repository.ProductRepository;

public class GetProductsUseCase {
    private final ProductRepository repository;

    public GetProductsUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Product>> execute() {
        return repository.getProducts();
    }
}