package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.domain.model.Comment;
import ru.mirea.nagishevakv.backeryproject.domain.repository.CommentRepository;

public class GetCommentsUseCase {
    private final CommentRepository repository;

    public GetCommentsUseCase(CommentRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Comment>> execute(int productId) {
        return repository.getComments(productId);
    }
}