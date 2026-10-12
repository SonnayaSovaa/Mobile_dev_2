package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import ru.mirea.nagishevakv.backeryproject.domain.repository.UserRepository;

public class LoginUseCase {
    private final UserRepository repository;

    public LoginUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> execute(String email, String password) {
        return repository.login(email, password);
    }
}