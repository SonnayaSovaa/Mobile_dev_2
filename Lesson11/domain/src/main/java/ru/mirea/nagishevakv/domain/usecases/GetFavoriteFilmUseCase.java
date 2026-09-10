package ru.mirea.nagishevakv.domain.usecases;

import ru.mirea.nagishevakv.domain.models.Movie;
import ru.mirea.nagishevakv.domain.repository.MovieRepository;

public class GetFavoriteFilmUseCase {
    private MovieRepository movieRepository;
    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public Movie execute(){
        return movieRepository.getMovie();
    }
}
