package ru.mirea.nagishevakv.domain.usecases;

import ru.mirea.nagishevakv.domain.models.Movie;
import ru.mirea.nagishevakv.domain.repository.MovieRepository;

public class SaveFilmToFavoriteUseCase {
    private MovieRepository movieRepository;
    public SaveFilmToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public boolean execute(Movie movie){
        return movieRepository.saveMovie(movie);
    }
}
