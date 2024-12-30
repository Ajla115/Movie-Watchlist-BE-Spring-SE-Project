package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.core.api.genresuggester.GenreSuggester;
import ba.edu.ibu.movieswatchlist.core.model.Genre;
import ba.edu.ibu.movieswatchlist.core.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreSuggester genreSuggester;

    public GenreService(GenreRepository genreRepository, GenreSuggester genreSuggester) {
        this.genreRepository = genreRepository;
        this.genreSuggester = genreSuggester;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre addGenre(Genre genre) {
        genre.setGenreId(null);
        return genreRepository.save(genre);
    }

    public Optional<Genre> getGenreByName(String name) {
        return genreRepository.findByName(name);
    }

    public Optional<Genre> getGenreById(Long genreId) {
        return genreRepository.findById(genreId);
    }

    public String suggestGenre(String movieTitle) {
        return genreSuggester.suggestGenre(movieTitle);
    }


}