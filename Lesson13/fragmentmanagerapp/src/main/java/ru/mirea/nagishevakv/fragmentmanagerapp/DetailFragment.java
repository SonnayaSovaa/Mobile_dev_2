package ru.mirea.nagishevakv.fragmentmanagerapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class DetailFragment extends Fragment {

    private static final String TAG = "DetailFragment";
    private TextView nameTextView;
    private TextView capitalTextView;
    private TextView descriptionTextView;
    private ShareViewModel viewModel;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        nameTextView = view.findViewById(R.id.countryNameTextView);
        capitalTextView = view.findViewById(R.id.countryCapitalTextView);
        descriptionTextView = view.findViewById(R.id.countryDescriptionTextView);

        viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                Log.d(TAG, "Selected country: " + country);
                nameTextView.setText(country);
                
                switch (country) {
                    case "Россия":
                        capitalTextView.setText("Столица: Москва");
                        descriptionTextView.setText("Россия — крупнейшее по площади государство в мире, расположенное в Восточной Европе и Северной Азии.");
                        break;
                    case "Китай":
                        capitalTextView.setText("Столица: Пекин");
                        descriptionTextView.setText("Китай — государство в Восточной Азии, одно из крупнейших по площади и численности населения в мире.");
                        break;
                    case "Бразилия":
                        capitalTextView.setText("Столица: Бразилиа");
                        descriptionTextView.setText("Бразилия — крупнейшее по площади и численности населения государство в Южной Америке.");
                        break;
                    case "Индия":
                        capitalTextView.setText("Столица: Нью-Дели");
                        descriptionTextView.setText("Индия — государство в Южной Азии, занимающее второе место в мире по численности населения.");
                        break;
                    case "ЮАР":
                        capitalTextView.setText("Столица: Претория");
                        descriptionTextView.setText("Южно-Африканская Республика — государство на юге Африканского континента.");
                        break;
                    default:
                        capitalTextView.setText("Столица: Неизвестно");
                        descriptionTextView.setText("Описание отсутствует.");
                        break;
                }
            }
        });
    }
}