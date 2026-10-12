package ru.mirea.nagishevakv.backeryproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.mirea.nagishevakv.backeryproject.data.local.dao.BakeryDao;
import ru.mirea.nagishevakv.backeryproject.data.local.db.BakeryDatabase;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.UserEntity;
import ru.mirea.nagishevakv.backeryproject.domain.model.User;
import ru.mirea.nagishevakv.backeryproject.domain.repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    private static final String TAG = "UserRepository";
    private static UserRepositoryImpl INSTANCE;
    private final BakeryDao bakeryDao;
    private final SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final MutableLiveData<User> clientInfo = new MutableLiveData<>();

    private UserRepositoryImpl(Context context) {
        this.bakeryDao = BakeryDatabase.getDatabase(context).bakeryDao();
        this.sharedPreferences = context.getSharedPreferences("bakery_prefs", Context.MODE_PRIVATE);
        
        try {
            if (!FirebaseApp.getApps(context).isEmpty()) {
                this.firebaseAuth = FirebaseAuth.getInstance();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase Auth", e);
        }
        loadClientInfo();
    }

    public static synchronized UserRepositoryImpl getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UserRepositoryImpl(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private void loadClientInfo() {
        String id = sharedPreferences.getString("user_id", "");
        String name = sharedPreferences.getString("user_name", "Гость");
        String email = sharedPreferences.getString("user_email", "");
        String photo = sharedPreferences.getString("user_photo", "");
        float rating = sharedPreferences.getFloat("user_rating", 5.00f);
        clientInfo.postValue(new User(id, name, email, photo, 0, (double) rating));
    }

    @Override
    public LiveData<Boolean> login(String email, String password) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            result.setValue(false);
            return result;
        }

        if (firebaseAuth != null) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        User user = new User(firebaseUser.getUid(), "User", firebaseUser.getEmail(), "", 0, 5.00);
                        saveClientInfo(user);
                        result.setValue(true);
                    } else {
                        performLocalAuth(email, email.split("@")[0], result);
                    }
                });
        } else {
            performLocalAuth(email, email.split("@")[0], result);
        }
        return result;
    }

    @Override
    public LiveData<Boolean> register(String email, String password, String nickname) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            result.setValue(false);
            return result;
        }

        if (firebaseAuth != null) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        User user = new User(uid, nickname, email, "", 0, 5.00);
                        saveClientInfo(user);
                        result.setValue(true);
                    } else {
                        performLocalAuth(email, nickname, result);
                    }
                });
        } else {
            performLocalAuth(email, nickname, result);
        }
        return result;
    }

    private void performLocalAuth(String email, String nickname, MutableLiveData<Boolean> result) {
        String uid = "local_" + Math.abs(email.hashCode());
        User user = new User(uid, nickname, email, "", 0, 5.00);
        saveClientInfo(user);
        result.setValue(true);
    }

    @Override
    public void logout() {
        if (firebaseAuth != null) {
            try {
                firebaseAuth.signOut();
            } catch (Exception e) {
                Log.e(TAG, "Error during Firebase sign out", e);
            }
        }
        sharedPreferences.edit().clear().apply();
        loadClientInfo();
    }

    @Override
    public boolean isAuthorized() {
        String id = sharedPreferences.getString("user_id", "");
        return !id.isEmpty() && !id.startsWith("guest_");
    }

    @Override
    public LiveData<User> getClientInfo() {
        return clientInfo;
    }

    @Override
    public void saveClientInfo(User user) {
        double roundedRating = Math.round(user.getRating() * 100.0) / 100.0;
        sharedPreferences.edit()
                .putString("user_id", user.getId())
                .putString("user_name", user.getNickname())
                .putString("user_email", user.getEmail())
                .putString("user_photo", user.getPhotoUrl())
                .putFloat("user_rating", (float) roundedRating)
                .apply();
        loadClientInfo();
        if (user.getId() != null && !user.getId().startsWith("guest_")) {
            executor.execute(() -> {
                try {
                    bakeryDao.insertUser(new UserEntity(user.getId(), user.getNickname(), user.getEmail(), user.getPhotoUrl(), 0, roundedRating));
                } catch (Exception e) {
                    Log.e(TAG, "Error saving user to DB", e);
                }
            });
        }
    }

    @Override
    public void updateUserPhoto(String photoUrl) {
        sharedPreferences.edit().putString("user_photo", photoUrl).apply();
        loadClientInfo();
        String userId = sharedPreferences.getString("user_id", "");
        if (!userId.isEmpty()) {
            executor.execute(() -> {
                UserEntity entity = bakeryDao.getUserByIdSync(userId);
                if (entity != null) {
                    entity.photoUrl = photoUrl;
                    bakeryDao.insertUser(entity);
                }
            });
        }
    }

    @Override
    public void updateUserRating(double delta) {
        float currentRating = sharedPreferences.getFloat("user_rating", 5.00f);
        double newRating = Math.round((currentRating + delta) * 100.0) / 100.0;
        newRating = Math.max(0.00, Math.min(10.00, newRating));
        
        sharedPreferences.edit().putFloat("user_rating", (float) newRating).apply();
        loadClientInfo();
        
        String userId = sharedPreferences.getString("user_id", "");
        if (!userId.isEmpty()) {
            double finalRating = newRating;
            executor.execute(() -> {
                UserEntity entity = bakeryDao.getUserByIdSync(userId);
                if (entity != null) {
                    entity.rating = finalRating;
                    bakeryDao.insertUser(entity);
                }
            });
        }
    }
}
