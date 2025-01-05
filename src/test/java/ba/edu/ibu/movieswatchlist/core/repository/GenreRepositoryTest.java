package ba.edu.ibu.movieswatchlist.core.repository;

import ba.edu.ibu.movieswatchlist.core.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GenreRepositoryTest {

    @Mock
    private GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByName_GenreExists() {

        String genreName = "Action";
        Genre mockGenre = new Genre();
        mockGenre.setGenreId(1L);
        mockGenre.setName(genreName);


        when(genreRepository.findByName(genreName)).thenReturn(Optional.of(mockGenre));


        Optional<Genre> foundGenre = genreRepository.findByName(genreName);


        assertTrue(foundGenre.isPresent(), "Genre should be found");
        assertEquals(genreName, foundGenre.get().getName(), "Genre name should match");
    }

    @Test
    void testFindByName_GenreDoesNotExist() {

        String genreName = "NonExistent";

        when(genreRepository.findByName(genreName)).thenReturn(Optional.empty());

        Optional<Genre> foundGenre = genreRepository.findByName(genreName);

        assertFalse(foundGenre.isPresent(), "Genre should not be found");
    }

    @Test
    void testExistsByName_True() {

        String genreName = "Comedy";

        when(genreRepository.existsByName(genreName)).thenReturn(true);

        boolean exists = genreRepository.existsByName(genreName);

        assertTrue(exists, "Genre should exist");
    }

    @Test
    void testExistsByName_False() {
        String genreName = "NonExistent";

        when(genreRepository.existsByName(genreName)).thenReturn(false);


        boolean exists = genreRepository.existsByName(genreName);

        assertFalse(exists, "Genre should not exist");
    }

    @Test
    void testFindAllSortedByName() {

        Genre genre1 = new Genre();
        genre1.setName("Action");
        Genre genre2 = new Genre();
        genre2.setName("Comedy");
        Genre genre3 = new Genre();
        genre3.setName("Drama");

        when(genreRepository.findAllSortedByName()).thenReturn(List.of(genre1, genre2, genre3));


        List<Genre> sortedGenres = genreRepository.findAllSortedByName();


        assertEquals(3, sortedGenres.size(), "There should be three genres");
        assertEquals("Action", sortedGenres.get(0).getName(), "First genre should be Action");
        assertEquals("Comedy", sortedGenres.get(1).getName(), "Second genre should be Comedy");
        assertEquals("Drama", sortedGenres.get(2).getName(), "Third genre should be Drama");
    }
}
