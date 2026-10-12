package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.model.Product;
import ru.mirea.nagishevakv.backeryproject.domain.repository.ProductRepository;

public class ManageCartUseCase {
    private final ProductRepository repository;

    public ManageCartUseCase(ProductRepository repository) {
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