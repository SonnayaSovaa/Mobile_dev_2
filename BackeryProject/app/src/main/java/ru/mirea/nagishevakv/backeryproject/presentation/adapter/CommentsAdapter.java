package ru.mirea.nagishevakv.backeryproject.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.nagishevakv.backeryproject.R;
import ru.mirea.nagishevakv.backeryproject.domain.model.Comment;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private final List<Comment> comments = new ArrayList<>();
    private String currentUserId;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Comment comment);
    }

    public void setData(List<Comment> newComments, String currentUserId, OnDeleteClickListener listener) {
        this.comments.clear();
        if (newComments != null) {
            this.comments.addAll(newComments);
        }
        this.currentUserId = currentUserId;
        this.deleteClickListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(comments.get(position), currentUserId, deleteClickListener);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivAvatar;
        private final TextView tvNickname;
        private final TextView tvDate;
        private final TextView tvText;
        private final ImageButton btnDelete;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvNickname = itemView.findViewById(R.id.tv_nickname);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvText = itemView.findViewById(R.id.tv_comment_text);
            btnDelete = itemView.findViewById(R.id.btn_delete_comment);
        }

        public void bind(Comment comment, String currentUserId, OnDeleteClickListener listener) {
            tvNickname.setText(comment.getUserName());
            tvDate.setText(comment.getDate());
            tvText.setText(comment.getText());
            ivAvatar.setImageResource(android.R.drawable.ic_menu_gallery);

            if (currentUserId != null && currentUserId.equals(comment.getUserId())) {
                btnDelete.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDeleteClick(comment);
                    }
                });
            } else {
                btnDelete.setVisibility(View.GONE);
            }
        }
    }
}