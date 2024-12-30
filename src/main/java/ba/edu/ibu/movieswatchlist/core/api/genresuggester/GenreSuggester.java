package ba.edu.ibu.movieswatchlist.core.api.genresuggester;

public interface GenreSuggester {
    String suggestGenre(String title);
    String suggestMovies(String title);
}

