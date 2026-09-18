package ru.mirea.nagishevakv.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private ShareViewModel viewModel;

    public ListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ListView listView = view.findViewById(R.id.countriesListView);

        // Подготовка данных для списка стран
        List<String> countries = new ArrayList<>();
        countries.add("Россия");
        countries.add("Китай");
        countries.add("Бразилия");
        countries.add("Индия");
        countries.add("ЮАР");

        if (getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    countries
            );
            listView.setAdapter(adapter);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedCountry = countries.get(position);
            viewModel.setSomeValue(selectedCountry);
        });

        return view;
    }
}