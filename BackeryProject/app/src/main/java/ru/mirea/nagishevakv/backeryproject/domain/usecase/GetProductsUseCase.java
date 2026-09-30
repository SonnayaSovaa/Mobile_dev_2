package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.repository.BakeryRepository;

public class GetProductsUseCase {
    private final BakeryRepository repository;

    public GetProductsUseCase(BakeryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Product>> execute() {
        return repository.getProducts();
    }
}