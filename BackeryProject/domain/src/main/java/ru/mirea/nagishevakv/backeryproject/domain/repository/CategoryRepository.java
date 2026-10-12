package ru.mirea.nagishevakv.backeryproject.domain.repository;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;

public interface CategoryRepository {
    LiveData<List<Category>> getCategories();
}
