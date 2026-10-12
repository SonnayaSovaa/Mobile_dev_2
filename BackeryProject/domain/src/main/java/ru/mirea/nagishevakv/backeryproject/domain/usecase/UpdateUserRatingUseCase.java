package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.repository.UserRepository;

public class UpdateUserRatingUseCase {
    private final UserRepository repository;

    public UpdateUserRatingUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(double delta) {
        repository.updateUserRating(delta);
    }
}