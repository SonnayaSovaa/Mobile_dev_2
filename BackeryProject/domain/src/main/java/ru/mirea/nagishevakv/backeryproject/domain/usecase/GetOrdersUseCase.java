package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.domain.model.Order;
import ru.mirea.nagishevakv.backeryproject.domain.repository.OrderRepository;

public class GetOrdersUseCase {
    private final OrderRepository repository;

    public GetOrdersUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Order>> execute() {
        return repository.getOrders();
    }
}