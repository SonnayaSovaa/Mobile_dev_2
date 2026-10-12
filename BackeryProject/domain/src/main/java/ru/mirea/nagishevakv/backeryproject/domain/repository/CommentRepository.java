package ru.mirea.nagishevakv.backeryproject.domain.repository;

import androidx.lifecycle.LiveData;
import java.util.List;
import ru.mirea.nagishevakv.backeryproject.domain.model.Comment;

public interface CommentRepository {
    LiveData<List<Comment>> getComments(int productId);
    void addComment(Comment comment);
    void deleteComment(int commentId);
}
