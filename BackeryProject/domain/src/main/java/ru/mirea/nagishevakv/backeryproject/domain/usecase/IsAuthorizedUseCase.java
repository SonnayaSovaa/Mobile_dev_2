package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.repository.UserRepository;

public class IsAuthorizedUseCase {
    private final UserRepository repository;

    public IsAuthorizedUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public boolean execute() {
        return repository.isAuthorized();
    }
}