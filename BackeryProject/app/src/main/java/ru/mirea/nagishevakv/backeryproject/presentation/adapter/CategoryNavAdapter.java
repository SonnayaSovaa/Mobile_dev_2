package ru.mirea.nagishevakv.backeryproject.presentation.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.nagishevakv.backeryproject.R;
import ru.mirea.nagishevakv.backeryproject.domain.model.Category;

public class CategoryNavAdapter extends RecyclerView.Adapter<CategoryNavAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(int categoryId);
    }

    private final List<Category> categories = new ArrayList<>();
    private final OnCategoryClickListener listener;
    private int selectedCategoryId = -1;

    public CategoryNavAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<Category> newCategories) {
        categories.clear();
        categories.add(new Category(-1, "Все"));
        if (newCategories != null) {
            categories.addAll(newCategories);
        }
        notifyDataSetChanged();
    }

    public void setSelectedCategoryId(int id) {
        this.selectedCategoryId = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_nav, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(categories.get(position), listener, selectedCategoryId);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button btnCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCategory = (Button) itemView;
        }

        public void bind(Category category, OnCategoryClickListener listener, int selectedId) {
            btnCategory.setText(category.getName());
            
            // Text color is always brown as requested
            int brownColor = Color.parseColor("#5D4037");
            btnCategory.setTextColor(brownColor);

            if (category.getId() == selectedId) {
                // When selected, background is orange (to keep brown text readable)
                btnCategory.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800")));
            } else {
                // When not selected, background is light orange
                btnCategory.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FFE0B2")));
            }
            btnCategory.setOnClickListener(v -> listener.onCategoryClick(category.getId()));
        }
    }
}
