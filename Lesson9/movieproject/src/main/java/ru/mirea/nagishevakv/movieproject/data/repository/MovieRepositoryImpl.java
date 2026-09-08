package ru.mirea.nagishevakv.movieproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.nagishevakv.movieproject.domain.models.Movie;
import ru.mirea.nagishevakv.movieproject.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private static final String SHARED_PREFS_NAME = "favorite_movie_prefs";
    private static final String KEY_MOVIE_NAME = "movie_name";
    private static final String KEY_MOVIE_ID = "movie_id";

    private Context context;

    public MovieRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MOVIE_NAME, movie.getName());
        editor.putInt(KEY_MOVIE_ID, movie.getId());
        return editor.commit();
    }

    @Override
    public Movie getMovie() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String movieName = sharedPreferences.getString(KEY_MOVIE_NAME, "Default Movie");
        int movieId = sharedPreferences.getInt(KEY_MOVIE_ID, -1);
        return new Movie(movieId, movieName);
    }
}