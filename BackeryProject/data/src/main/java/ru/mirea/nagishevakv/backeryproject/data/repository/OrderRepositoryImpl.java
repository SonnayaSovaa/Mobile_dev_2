package ru.mirea.nagishevakv.backeryproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import ru.mirea.nagishevakv.backeryproject.data.local.dao.BakeryDao;
import ru.mirea.nagishevakv.backeryproject.data.local.db.BakeryDatabase;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.OrderEntity;
import ru.mirea.nagishevakv.backeryproject.domain.model.Order;
import ru.mirea.nagishevakv.backeryproject.domain.repository.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {
    private static OrderRepositoryImpl INSTANCE;
    private final BakeryDao bakeryDao;
    private final SharedPreferences sharedPreferences;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private OrderRepositoryImpl(Context context) {
        this.bakeryDao = BakeryDatabase.getDatabase(context).bakeryDao();
        this.sharedPreferences = context.getSharedPreferences("bakery_prefs", Context.MODE_PRIVATE);
    }

    public static synchronized OrderRepositoryImpl getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new OrderRepositoryImpl(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<Order>> getOrders() {
        String userId = sharedPreferences.getString("user_id", "");
        MediatorLiveData<List<Order>> mediator = new MediatorLiveData<>();
        mediator.addSource(bakeryDao.getAllOrders(), entities -> {
            if (entities != null) {
                List<Order> filtered = entities.stream()
                        .filter(e -> e.userId.equals(userId))
                        .map(e -> new Order(e.id, e.userId, e.cost, e.itemCount, e.itemsDescription, e.date, e.city, e.status))
                        .collect(Collectors.toList());
                mediator.setValue(filtered);
            }
        });
        return mediator;
    }

    @Override
    public void createOrder(double cost, int itemCount, String itemsDescription, String city) {
        String userId = sharedPreferences.getString("user_id", "");
        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date());
        executor.execute(() -> {
            bakeryDao.insertOrder(new OrderEntity(userId, cost, itemCount, itemsDescription, date, city, "Активен"));
        });
    }
}
