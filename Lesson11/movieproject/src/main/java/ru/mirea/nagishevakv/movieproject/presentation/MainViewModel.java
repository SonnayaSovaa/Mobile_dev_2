package ru.mirea.nagishevakv.movieproject.presentation;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.nagishevakv.domain.models.Movie;
import ru.mirea.nagishevakv.domain.repository.MovieRepository;
import ru.mirea.nagishevakv.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.nagishevakv.domain.usecases.SaveFilmToFavoriteUseCase;

public class MainViewModel extends ViewModel {
    private MovieRepository movieRepository;

    public MainViewModel(MovieRepository movieRepository) {
        Log.d(MainViewModel.class.getSimpleName().toString(), "MainViewModel created");
        this.movieRepository = movieRepository;
    }

    private MutableLiveData<String> favoriteMovie = new MutableLiveData<>();

    public MutableLiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    }

    @Override
    protected void onCleared() {
        Log.d(MainViewModel.class.getSimpleName().toString(), "MainViewModel cleared");
        super.onCleared();
    }

    public void setText(Movie movie) {
        Boolean result = new
                SaveFilmToFavoriteUseCase(movieRepository).execute(movie);
        favoriteMovie.setValue(result.toString());
    }

    public void getText() {
        Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
        favoriteMovie.setValue(String.format("My favorite movie is %s",
                movie.getName()));
    }
}