package ru.mirea.nagishevakv.recyclerviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<HistoryEventViewHolder>{

    private List<HistoryEvent> historyEvents;
    private Context context;
    public EventRecyclerViewAdapter(List<HistoryEvent> historyEvents) {
        this.historyEvents = historyEvents;
    }
    @Override
    public HistoryEventViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        context = parent.getContext();
        View recyclerViewItem =
                LayoutInflater.from(context).inflate(R.layout.item, parent,false);
        return new HistoryEventViewHolder(recyclerViewItem);
    }
    @Override
    public int getItemCount() {
        return this.historyEvents.size();
    }

    @Override
    public void onBindViewHolder(HistoryEventViewHolder holder, int position) {
// Cet historyEvent in countries via position
        HistoryEvent historyEvent = this.historyEvents.get(position);
        String pkgName = context.getPackageName();
// Return 0 if not found.
        int resID = context.getResources().getIdentifier(historyEvent.getImgName() ,
                "drawable", pkgName);
// Bind data to viewholder
        holder.getImgView().setImageResource(resID);
        holder.getNameView().setText(historyEvent.getHistoryEventName());
        holder.getDateView().setText("Date: " + historyEvent.getEventDate());
    }
}
