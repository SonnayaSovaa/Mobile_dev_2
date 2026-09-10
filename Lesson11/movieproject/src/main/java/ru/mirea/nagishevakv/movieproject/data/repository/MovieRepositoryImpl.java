package ru.mirea.nagishevakv.movieproject.data.repository;

import java.time.LocalDate;

import ru.mirea.nagishevakv.movieproject.data.storage.MovieStorage;
import ru.mirea.nagishevakv.movieproject.domain.models.Movie;
import ru.mirea.nagishevakv.movieproject.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private final MovieStorage movieStorage;

    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        ru.mirea.nagishevakv.movieproject.data.storage.models.Movie storageMovie = mapToStorage(movie);
        return movieStorage.save(storageMovie);
    }

    @Override
    public Movie getMovie() {
        ru.mirea.nagishevakv.movieproject.data.storage.models.Movie storageMovie = movieStorage.get();
        return mapToDomain(storageMovie);
    }

    private Movie mapToDomain(ru.mirea.nagishevakv.movieproject.data.storage.models.Movie movie) {
        return new Movie(movie.getId(), movie.getName());
    }

    private ru.mirea.nagishevakv.movieproject.data.storage.models.Movie mapToStorage(Movie movie) {
        return new ru.mirea.nagishevakv.movieproject.data.storage.models.Movie(movie.getId(), movie.getName(), LocalDate.now().toString());
    }
}
