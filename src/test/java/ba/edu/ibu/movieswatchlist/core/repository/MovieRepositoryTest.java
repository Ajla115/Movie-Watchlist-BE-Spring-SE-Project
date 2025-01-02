package ba.edu.ibu.movieswatchlist.core.repository;

import static org.junit.jupiter.api.Assertions.*;
import ba.edu.ibu.movieswatchlist.core.model.Movie;
import ba.edu.ibu.movieswatchlist.core.model.User;
import ba.edu.ibu.movieswatchlist.core.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.mockito.Mockito.when;

class MovieRepositoryTest {

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUser() {
        User mockUser = new User();
        mockUser.setUserId(1L);
        Movie movie1 = new Movie();
        movie1.setTitle("Movie 1");
        Movie movie2 = new Movie();
        movie2.setTitle("Movie 2");

        when(movieRepository.findByUser(mockUser)).thenReturn(List.of(movie1, movie2));

        List<Movie> movies = movieRepository.findByUser(mockUser);

        assertEquals(2, movies.size());
        assertEquals("Movie 1", movies.get(0).getTitle());
        assertEquals("Movie 2", movies.get(1).getTitle());
    }

    @Test
    void testFindByStatusAndUserId() {
        Movie movie = new Movie();
        movie.setTitle("Movie 1");
        movie.setStatus("Watched");

        when(movieRepository.findByStatusAndUserId(1L, "Watched")).thenReturn(List.of(movie));

        List<Movie> movies = movieRepository.findByStatusAndUserId(1L, "Watched");

        assertEquals(1, movies.size());
        assertEquals("Watched", movies.get(0).getStatus());
    }

    @Test
    void testFindByWatchlistOrderAndUserId() {
        Movie movie = new Movie();
        movie.setTitle("Movie 1");
        movie.setWatchlistOrder("Next Up");

        when(movieRepository.findByWatchlistOrderAndUserId(1L, "Next Up")).thenReturn(List.of(movie));

        List<Movie> movies = movieRepository.findByWatchlistOrderAndUserId(1L, "Next Up");

        assertEquals(1, movies.size());
        assertEquals("Next Up", movies.get(0).getWatchlistOrder());
    }

    @Test
    void testFindByGenreAndUserId() {
        Movie movie = new Movie();
        movie.setTitle("Movie 1");
        Genre mockGenre = new Genre();
        mockGenre.setGenreId(1L);
        movie.setGenre(mockGenre);

        when(movieRepository.findByGenreAndUserId(1L, 1L)).thenReturn(List.of(movie));

        List<Movie> movies = movieRepository.findByGenreAndUserId(1L, 1L);

        assertEquals(1, movies.size());
        assertEquals(1L, movies.get(0).getGenre().getGenreId());
    }

    @Test
    void testFindAllMoviesByUserSortedByTitle() {
        User mockUser = new User();
        mockUser.setUserId(1L);
        Movie movie1 = new Movie();
        movie1.setTitle("A Movie");
        Movie movie2 = new Movie();
        movie2.setTitle("B Movie");

        when(movieRepository.findAllMoviesByUserSortedByTitle(mockUser)).thenReturn(List.of(movie1, movie2));

        List<Movie> movies = movieRepository.findAllMoviesByUserSortedByTitle(mockUser);

        assertEquals(2, movies.size());
        assertEquals("A Movie", movies.get(0).getTitle());
        assertEquals("B Movie", movies.get(1).getTitle());
    }

    @Test
    void testFindAllByUserIdOrderByWatchlistOrderAsc() {
        Movie movie1 = new Movie();
        movie1.setWatchlistOrder("1");
        Movie movie2 = new Movie();
        movie2.setWatchlistOrder("2");

        when(movieRepository.findAllByUserIdOrderByWatchlistOrderAsc(1L)).thenReturn(List.of(movie1, movie2));

        List<Movie> movies = movieRepository.findAllByUserIdOrderByWatchlistOrderAsc(1L);

        assertEquals(2, movies.size());
        assertEquals("1", movies.get(0).getWatchlistOrder());
        assertEquals("2", movies.get(1).getWatchlistOrder());
    }

    @Test
    void testFindAllByUserIdOrderByWatchlistOrderDesc() {
        Movie movie1 = new Movie();
        movie1.setWatchlistOrder("2");
        Movie movie2 = new Movie();
        movie2.setWatchlistOrder("1");

        when(movieRepository.findAllByUserIdOrderByWatchlistOrderDesc(1L)).thenReturn(List.of(movie1, movie2));

        List<Movie> movies = movieRepository.findAllByUserIdOrderByWatchlistOrderDesc(1L);

        assertEquals(2, movies.size());
        assertEquals("2", movies.get(0).getWatchlistOrder());
        assertEquals("1", movies.get(1).getWatchlistOrder());
    }
}
