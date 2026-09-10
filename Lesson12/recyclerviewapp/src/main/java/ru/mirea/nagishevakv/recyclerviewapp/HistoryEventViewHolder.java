package ru.mirea.nagishevakv.recyclerviewapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryEventViewHolder extends RecyclerView.ViewHolder {
    private ImageView imgView;
    private TextView eventNameView;
    private TextView dateView;

    public HistoryEventViewHolder(@NonNull View itemView) {
        super(itemView);
        this.imgView = itemView.findViewById(R.id.imageView);
        this.eventNameView = itemView.findViewById(R.id.name);
        this.dateView = itemView.findViewById(R.id.date);
    }

    public ImageView getImgView() {
        return imgView;
    }

    public TextView getNameView() {
        return eventNameView;
    }

    public TextView getDateView() {
        return dateView;

    }
}