package ru.mirea.nagishevakv.retrofitapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlankFragment extends Fragment {

    public static final String TAG = "BlankFragment";
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String ARG_NUMBER_STUDENT = "my_number_student";

    private int numberStudent;
    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private ApiService apiService;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        
        Log.d(TAG, "Student Number: " + numberStudent);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        loadTodos();
        
        return view;
    }

    private void loadTodos() {
        Call<List<Todo>> call = apiService.getTodos();
        call.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                if (response.isSuccessful() && response.body() != null && isAdded()) {
                    List<Todo> todos = response.body();
                    todoAdapter = new TodoAdapter(getContext(), todos, (todo, isChecked) -> {
                        todo.setCompleted(isChecked);
                        updateTodo(todo);
                    });
                    recyclerView.setAdapter(todoAdapter);
                } else {
                    Log.e(TAG, "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void updateTodo(Todo todo) {
        apiService.updateTodo(todo.getId(), todo).enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                if (response.isSuccessful() && isAdded()) {
                    Toast.makeText(getContext(), 
                            "Updated: " + response.body().getTitle(),
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Updated successfully: " + response.code());
                } else {
                    Log.e(TAG, "Update failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.e(TAG, "Update error: " + t.getMessage());
            }
        });
    }
}