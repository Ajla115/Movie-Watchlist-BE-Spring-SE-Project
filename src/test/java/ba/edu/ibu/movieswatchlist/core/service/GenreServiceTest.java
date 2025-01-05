package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.core.api.genresuggester.GenreSuggester;
import ba.edu.ibu.movieswatchlist.core.model.Genre;
import ba.edu.ibu.movieswatchlist.core.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreSuggester genreSuggester;

    @InjectMocks
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGenres() {
        Genre genre1 = new Genre();
        genre1.setGenreId(1L);
        genre1.setName("Action");

        Genre genre2 = new Genre();
        genre2.setGenreId(2L);
        genre2.setName("Comedy");

        when(genreRepository.findAll()).thenReturn(List.of(genre1, genre2));

        List<Genre> genres = genreService.getAllGenres();

        assertNotNull(genres);
        assertEquals(2, genres.size());
        assertEquals("Action", genres.get(0).getName());
        assertEquals("Comedy", genres.get(1).getName());
    }

    @Test
    void testAddGenre() {
        Genre genre = new Genre();
        genre.setName("Horror");

        when(genreRepository.save(any(Genre.class))).thenAnswer(invocation -> {
            Genre savedGenre = invocation.getArgument(0);
            savedGenre.setGenreId(1L);
            return savedGenre;
        });

        Genre savedGenre = genreService.addGenre(genre);

        assertNotNull(savedGenre);
        assertEquals(1L, savedGenre.getGenreId());
        assertEquals("Horror", savedGenre.getName());
    }

    @Test
    void testGetGenreByName_GenreExists() {
        String genreName = "Drama";
        Genre genre = new Genre();
        genre.setGenreId(1L);
        genre.setName(genreName);

        when(genreRepository.findByName(genreName)).thenReturn(Optional.of(genre));

        Optional<Genre> foundGenre = genreService.getGenreByName(genreName);

        assertTrue(foundGenre.isPresent());
        assertEquals(genreName, foundGenre.get().getName());
    }

    @Test
    void testGetGenreByName_GenreDoesNotExist() {
        String genreName = "NonExistent";

        when(genreRepository.findByName(genreName)).thenReturn(Optional.empty());

        Optional<Genre> foundGenre = genreService.getGenreByName(genreName);

        assertFalse(foundGenre.isPresent());
    }

    @Test
    void testGetGenreById_GenreExists() {
        Long genreId = 1L;
        Genre genre = new Genre();
        genre.setGenreId(genreId);
        genre.setName("Thriller");

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));

        Optional<Genre> foundGenre = genreService.getGenreById(genreId);

        assertTrue(foundGenre.isPresent());
        assertEquals("Thriller", foundGenre.get().getName());
    }

    @Test
    void testGetGenreById_GenreDoesNotExist() {
        Long genreId = 1L;

        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        Optional<Genre> foundGenre = genreService.getGenreById(genreId);

        assertFalse(foundGenre.isPresent());
    }

    @Test
    void testSuggestGenre() {
        String movieTitle = "Inception";
        String suggestedGenre = "Science Fiction";

        when(genreSuggester.suggestGenre(movieTitle)).thenReturn(suggestedGenre);

        String result = genreService.suggestGenre(movieTitle);

        assertEquals(suggestedGenre, result);
    }
}
