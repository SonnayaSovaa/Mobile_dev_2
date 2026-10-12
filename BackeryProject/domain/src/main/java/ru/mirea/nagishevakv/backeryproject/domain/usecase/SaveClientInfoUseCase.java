package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.model.User;
import ru.mirea.nagishevakv.backeryproject.domain.repository.UserRepository;

public class SaveClientInfoUseCase {
    private final UserRepository repository;

    public SaveClientInfoUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(User user) {
        repository.saveClientInfo(user);
    }
}