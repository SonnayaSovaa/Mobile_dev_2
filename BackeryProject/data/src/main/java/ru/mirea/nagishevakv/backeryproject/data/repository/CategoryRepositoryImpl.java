package ru.mirea.nagishevakv.backeryproject.data.repository;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.data.network.NetworkApi;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.repository.CategoryRepository;

public class CategoryRepositoryImpl implements CategoryRepository {
    private static CategoryRepositoryImpl INSTANCE;
    private final NetworkApi networkApi;

    private CategoryRepositoryImpl() {
        this.networkApi = new NetworkApi();
    }

    public static synchronized CategoryRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CategoryRepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return networkApi.getCategories();
    }
}
