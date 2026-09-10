package ru.mirea.nagishevakv.domain.repository;

import ru.mirea.nagishevakv.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}
