package ru.mirea.nagishevakv.recyclerviewapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<HistoryEvent> countries = getListData();
        RecyclerView recyclerView = this.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new EventRecyclerViewAdapter(countries));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(layoutManager);
    }
    private List<HistoryEvent> getListData() {
        List<HistoryEvent> list = new ArrayList<HistoryEvent>();
        HistoryEvent first = new HistoryEvent("Бородинская битва", "nextone", 1812);
        HistoryEvent second = new HistoryEvent("Полёт Гагарина", "flagwithpanels", 1961);
        HistoryEvent third = new HistoryEvent("Крещение Руси", "shutters", 988);

        list.add(first);
        list.add(second);
        list.add(third);
        return list;
    }
}