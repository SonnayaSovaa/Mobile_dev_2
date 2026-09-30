package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import ru.mirea.nagishevakv.backeryproject.domain.model.User;
import ru.mirea.nagishevakv.backeryproject.domain.repository.BakeryRepository;

public class GetClientInfoUseCase {
    private final BakeryRepository repository;

    public GetClientInfoUseCase(BakeryRepository repository) {
        this.repository = repository;
    }

    public LiveData<User> execute() {
        return repository.getClientInfo();
    }
}