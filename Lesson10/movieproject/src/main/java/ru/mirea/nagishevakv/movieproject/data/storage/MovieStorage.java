package ru.mirea.nagishevakv.movieproject.data.storage;

import ru.mirea.nagishevakv.movieproject.data.storage.models.Movie;

public interface MovieStorage {
    public Movie get();
    public boolean save(Movie movie);
}