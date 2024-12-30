package ba.edu.ibu.movieswatchlist.core.api.genresuggester;

import ba.edu.ibu.movieswatchlist.core.model.Genre;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenreSuggester {
    String suggestGenre(String title);
    String suggestMovies(String title);
}

