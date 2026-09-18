package ru.mirea.nagishevakv.retrofitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Todo> todos;
    private OnTodoCheckedChangeListener listener;

    public interface OnTodoCheckedChangeListener {
        void onTodoChecked(Todo todo, boolean isChecked);
    }

    public TodoAdapter(Context context, List<Todo> todoList, OnTodoCheckedChangeListener listener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.todos = todoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.textViewTitle.setText(todo.getTitle());

        // Unset listener to avoid triggering it when setting initial state
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(todo.getCompleted());

        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onTodoChecked(todo, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }
}