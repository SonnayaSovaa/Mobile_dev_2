package ru.mirea.nagishevakv.backeryproject.domain.repository;

import androidx.lifecycle.LiveData;
import ru.mirea.nagishevakv.backeryproject.domain.model.User;

public interface UserRepository {
    LiveData<Boolean> login(String email, String password);
    LiveData<Boolean> register(String email, String password, String nickname);
    void logout();
    boolean isAuthorized();
    LiveData<User> getClientInfo();
    void saveClientInfo(User user);
    void updateUserPhoto(String photoUrl);
    void updateUserRating(double delta);
}
