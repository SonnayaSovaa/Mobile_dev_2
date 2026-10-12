package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import ru.mirea.nagishevakv.backeryproject.domain.repository.UserRepository;

public class RegisterUseCase {
    private final UserRepository repository;

    public RegisterUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> execute(String email, String password, String nickname) {
        return repository.register(email, password, nickname);
    }
}