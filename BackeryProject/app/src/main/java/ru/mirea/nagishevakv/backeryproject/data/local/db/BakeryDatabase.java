package ru.mirea.nagishevakv.backeryproject.data.local.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.mirea.nagishevakv.backeryproject.data.local.dao.BakeryDao;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.CategoryEntity;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.CommentEntity;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.OrderEntity;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.ProductEntity;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.UserEntity;

@Database(entities = {CategoryEntity.class, ProductEntity.class, UserEntity.class, CommentEntity.class, OrderEntity.class}, version = 2, exportSchema = false)
public abstract class BakeryDatabase extends RoomDatabase {
    private static volatile BakeryDatabase INSTANCE;

    public abstract BakeryDao bakeryDao();

    public static BakeryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BakeryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BakeryDatabase.class, "bakery_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}