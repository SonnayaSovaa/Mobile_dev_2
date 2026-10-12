package ru.mirea.nagishevakv.backeryproject.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import ru.mirea.nagishevakv.backeryproject.data.local.dao.BakeryDao;
import ru.mirea.nagishevakv.backeryproject.data.local.db.BakeryDatabase;
import ru.mirea.nagishevakv.backeryproject.data.local.entity.CommentEntity;
import ru.mirea.nagishevakv.backeryproject.domain.model.Comment;
import ru.mirea.nagishevakv.backeryproject.domain.repository.CommentRepository;

public class CommentRepositoryImpl implements CommentRepository {
    private static CommentRepositoryImpl INSTANCE;
    private final BakeryDao bakeryDao;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private CommentRepositoryImpl(Context context) {
        this.bakeryDao = BakeryDatabase.getDatabase(context).bakeryDao();
    }

    public static synchronized CommentRepositoryImpl getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CommentRepositoryImpl(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<Comment>> getComments(int productId) {
        MediatorLiveData<List<Comment>> mediator = new MediatorLiveData<>();
        mediator.addSource(bakeryDao.getCommentsForProduct(productId), entities -> {
            if (entities != null) {
                mediator.setValue(entities.stream()
                        .map(e -> new Comment(e.id, e.text, e.userId, e.productId, e.userName, e.userPhotoUrl, e.date, e.userRating))
                        .collect(Collectors.toList()));
            }
        });
        return mediator;
    }

    @Override
    public void addComment(Comment comment) {
        executor.execute(() -> {
            bakeryDao.insertComment(new CommentEntity(
                    comment.getText(),
                    comment.getUserId(),
                    comment.getProductId(),
                    comment.getUserName(),
                    comment.getUserPhotoUrl(),
                    comment.getDate(),
                    comment.getUserRating()
            ));
        });
    }

    @Override
    public void deleteComment(int commentId) {
        executor.execute(() -> {
            bakeryDao.deleteCommentById(commentId);
        });
    }
}
