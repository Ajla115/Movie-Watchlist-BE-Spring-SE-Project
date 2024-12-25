package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.core.model.Genre;
import ba.edu.ibu.movieswatchlist.core.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//dodati dva AI suggesters, mozda oba ovdje, ili jedan ovdje, a drugi tamo u movies
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public Optional<Genre> getGenreById(Long genreId) {
        return genreRepository.findById(genreId);
    }
}