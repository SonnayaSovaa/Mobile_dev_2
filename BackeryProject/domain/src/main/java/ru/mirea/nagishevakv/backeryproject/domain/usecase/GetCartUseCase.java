package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import java.util.Map;
import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.repository.BakeryRepository;

public class GetCartUseCase {
    private final BakeryRepository repository;

    public GetCartUseCase(BakeryRepository repository) {
        this.repository = repository;
    }

    public LiveData<Map<Product, Integer>> execute() {
        return repository.getCartItems();
    }
}