package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import ru.mirea.nagishevakv.backeryproject.domain.repository.BakeryRepository;

public class RegisterUseCase {
    private final BakeryRepository repository;

    public RegisterUseCase(BakeryRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> execute(String email, String password, String nickname) {
        return repository.register(email, password, nickname);
    }
}
