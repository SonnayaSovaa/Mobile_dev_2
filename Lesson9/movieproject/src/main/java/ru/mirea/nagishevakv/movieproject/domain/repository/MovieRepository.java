package ru.mirea.nagishevakv.movieproject.domain.repository;

import ru.mirea.nagishevakv.movieproject.domain.models.Movie;


public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}
