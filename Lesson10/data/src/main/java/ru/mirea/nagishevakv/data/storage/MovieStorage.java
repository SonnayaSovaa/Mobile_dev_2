package ru.mirea.nagishevakv.data.storage;

import ru.mirea.nagishevakv.data.storage.models.Movie;

public interface MovieStorage {
    public Movie get();
    public boolean save(Movie movie);
}
