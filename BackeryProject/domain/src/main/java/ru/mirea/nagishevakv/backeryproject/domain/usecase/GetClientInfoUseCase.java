package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import ru.mirea.nagishevakv.backeryproject.domain.model.User;
import ru.mirea.nagishevakv.backeryproject.domain.repository.UserRepository;

public class GetClientInfoUseCase {
    private final UserRepository repository;

    public GetClientInfoUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public LiveData<User> execute() {
        return repository.getClientInfo();
    }
}