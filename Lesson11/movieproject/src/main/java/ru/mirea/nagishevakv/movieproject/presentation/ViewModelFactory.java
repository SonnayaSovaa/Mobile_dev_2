package ru.mirea.nagishevakv.movieproject.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.nagishevakv.data.repository.MovieRepositoryImpl;
import ru.mirea.nagishevakv.data.storage.MovieStorage;
import ru.mirea.nagishevakv.data.storage.SharedPrefMovieStorage;
import ru.mirea.nagishevakv.domain.repository.MovieRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    public ViewModelFactory(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(context);
        MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);
        return (T) new MainViewModel(movieRepository);
    }
}