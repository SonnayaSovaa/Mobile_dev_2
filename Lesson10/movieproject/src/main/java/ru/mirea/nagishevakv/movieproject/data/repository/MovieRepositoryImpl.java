package ru.mirea.nagishevakv.movieproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.nagishevakv.movieproject.data.storage.MovieStorage;
import ru.mirea.nagishevakv.movieproject.domain.models.Movie;
import ru.mirea.nagishevakv.movieproject.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    MovieStorage movieStorage;

    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        movieStorage.save(movie);
        return true;
    }

    @Override
    public Movie getMovie() {
       return movieStorage.get();
    }
}