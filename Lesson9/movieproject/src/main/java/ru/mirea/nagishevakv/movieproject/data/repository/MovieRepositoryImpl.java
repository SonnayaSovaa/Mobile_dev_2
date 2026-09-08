package ru.mirea.nagishevakv.movieproject.data.repository;

import ru.mirea.nagishevakv.movieproject.domain.models.Movie;
import ru.mirea.nagishevakv.movieproject.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    @Override
    public boolean saveMovie(Movie movie){
        return true;
    }
    @Override
    public Movie getMovie(){
        return new Movie(1, "Game of throne");
    }
}