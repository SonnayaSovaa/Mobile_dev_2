package ru.mirea.nagishevakv.retrofitapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("todos")
    Call<List<Todo>> getTodos();


}