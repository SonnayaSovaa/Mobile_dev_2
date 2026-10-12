package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.model.Comment;
import ru.mirea.nagishevakv.backeryproject.domain.repository.CommentRepository;

public class AddCommentUseCase {
    private final CommentRepository repository;

    public AddCommentUseCase(CommentRepository repository) {
        this.repository = repository;
    }

    public void execute(Comment comment) {
        repository.addComment(comment);
    }
}