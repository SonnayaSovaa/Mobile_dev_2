package ru.mirea.nagishevakv.backeryproject.domain.repository;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.domain.model.Order;

public interface OrderRepository {
    LiveData<List<Order>> getOrders();
    void createOrder(double cost, int itemCount, String itemsDescription, String city);
}
