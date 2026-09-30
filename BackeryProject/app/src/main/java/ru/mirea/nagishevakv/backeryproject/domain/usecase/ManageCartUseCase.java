package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.repository.BakeryRepository;

public class ManageCartUseCase {
    private final BakeryRepository repository;

    public ManageCartUseCase(BakeryRepository repository) {
        this.repository = repository;
    }

    public void add(Product product) {
        repository.addToCart(product);
    }

    public void remove(Product product) {
        repository.removeFromCart(product);
    }

    public void clear() {
        repository.clearCart();
    }
}