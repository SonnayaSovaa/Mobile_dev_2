package ru.mirea.nagishevakv.movieproject.domain.usecases;

import ru.mirea.nagishevakv.movieproject.domain.repository.MovieRepository;
import ru.mirea.nagishevakv.movieproject.domain.models.Movie;

public class SaveFilmToFavoriteUseCase {
    private MovieRepository movieRepository;
    public SaveFilmToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public boolean execute(Movie movie){
        return movieRepository.saveMovie(movie);
    }
}