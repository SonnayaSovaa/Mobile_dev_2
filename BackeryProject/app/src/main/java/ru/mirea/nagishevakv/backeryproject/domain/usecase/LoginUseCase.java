package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import ru.mirea.nagishevakv.backeryproject.domain.repository.BakeryRepository;

public class LoginUseCase {
    private final BakeryRepository repository;

    public LoginUseCase(BakeryRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> execute(String email, String password) {
        return repository.loginWithEmailAndPassword(email, password);
    }
}