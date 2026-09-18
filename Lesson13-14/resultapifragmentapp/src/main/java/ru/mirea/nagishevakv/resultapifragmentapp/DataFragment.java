package ru.mirea.nagishevakv.resultapifragmentapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class DataFragment extends Fragment {

    private FragmentListener listener;

    public DataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        ImageView image = view.findViewById(R.id.imageView);
        image.setOnClickListener(view1 -> listener.sendResult("image pushed"));

        Button button = view.findViewById(R.id.buttonOpenBottomSheet);
        button.setOnClickListener(click -> {
            String text = ((EditText) view.findViewById(R.id.editTextInfo)).getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("key", text);
            
            // Отправка данных для BottomSheetFragment
            getChildFragmentManager().setFragmentResult("requestKey", bundle);
            
            BottomSheetFragment bottomSheet = new BottomSheetFragment();
            bottomSheet.show(getChildFragmentManager(), "ModalBottomSheet");
        });

        return view;
    }
}