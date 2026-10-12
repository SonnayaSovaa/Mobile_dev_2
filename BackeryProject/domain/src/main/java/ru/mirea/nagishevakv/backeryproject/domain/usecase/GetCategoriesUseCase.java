package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;
import ru.mirea.nagishevakv.backeryproject.domain.repository.CategoryRepository;

public class GetCategoriesUseCase {
    private final CategoryRepository repository;

    public GetCategoriesUseCase(CategoryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Category>> execute() {
        return repository.getCategories();
    }
}