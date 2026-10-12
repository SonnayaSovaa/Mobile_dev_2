package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.repository.UserRepository;

public class LogoutUseCase {
    private final UserRepository repository;

    public LogoutUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        repository.logout();
    }
}