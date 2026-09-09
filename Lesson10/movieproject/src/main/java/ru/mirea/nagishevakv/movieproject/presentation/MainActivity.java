package ru.mirea.nagishevakv.movieproject.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.nagishevakv.data.repository.MovieRepositoryImpl;
import ru.mirea.nagishevakv.data.storage.MovieStorage;
import ru.mirea.nagishevakv.data.storage.SharedPrefMovieStorage;
import ru.mirea.nagishevakv.domain.models.Movie;
import ru.mirea.nagishevakv.domain.repository.MovieRepository;
import ru.mirea.nagishevakv.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.nagishevakv.domain.usecases.SaveFilmToFavoriteUseCase;
import ru.mirea.nagishevakv.movieproject.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(this);
        MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);

        EditText text = findViewById(R.id.te);
        TextView textView = findViewById(R.id.textView);

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = new
                        SaveFilmToFavoriteUseCase(movieRepository).execute(new Movie(2,
                        text.getText().toString()));
                textView.setText(String.format("Save result %s", result));
            }
        });
        findViewById(R.id.button_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie moview = new GetFavoriteFilmUseCase(movieRepository).execute();
                textView.setText(String.format("Get result %s", moview.getName()));
            }
        });
    }

}
