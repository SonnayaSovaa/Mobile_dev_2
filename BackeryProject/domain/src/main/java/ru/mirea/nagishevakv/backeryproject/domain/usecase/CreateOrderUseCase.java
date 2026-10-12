package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.repository.OrderRepository;

public class CreateOrderUseCase {
    private final OrderRepository repository;

    public CreateOrderUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    public void execute(double cost, int itemCount, String itemsDescription, String city) {
        repository.createOrder(cost, itemCount, itemsDescription, city);
    }
}