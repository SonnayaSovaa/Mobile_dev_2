package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.repository.UserRepository;

public class UpdateUserPhotoUseCase {
    private final UserRepository repository;

    public UpdateUserPhotoUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(String photoUrl) {
        repository.updateUserPhoto(photoUrl);
    }
}