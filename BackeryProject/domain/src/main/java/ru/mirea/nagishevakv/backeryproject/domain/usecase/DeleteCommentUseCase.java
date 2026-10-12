package ru.mirea.nagishevakv.backeryproject.domain.usecase;

import ru.mirea.nagishevakv.backeryproject.domain.repository.CommentRepository;

public class DeleteCommentUseCase {
    private final CommentRepository repository;

    public DeleteCommentUseCase(CommentRepository repository) {
        this.repository = repository;
    }

    public void execute(int commentId) {
        repository.deleteComment(commentId);
    }
}