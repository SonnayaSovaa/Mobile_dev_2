package ru.mirea.nagishevakv.retrofitapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BlankFragment extends Fragment {

    private static final String ARG_NUMBER_STUDENT = "my_number_student";
    private int numberStudent;

    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance(int numberStudent) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER_STUDENT, numberStudent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numberStudent = getArguments().getInt(ARG_NUMBER_STUDENT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(BlankFragment.class.getSimpleName(), "Student Number: " + numberStudent);
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }
}