package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.api.impl.infobip.InfobipEmailService;
import ba.edu.ibu.movieswatchlist.core.api.genresuggester.GenreSuggester;
import ba.edu.ibu.movieswatchlist.core.model.*;
import ba.edu.ibu.movieswatchlist.core.repository.GenreRepository;
import ba.edu.ibu.movieswatchlist.core.repository.MovieRepository;
import ba.edu.ibu.movieswatchlist.core.repository.UserRepository;
import ba.edu.ibu.movieswatchlist.core.repository.WatchlistEntryRepository;
import ba.edu.ibu.movieswatchlist.rest.dto.MovieDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreService genreService;

    @Mock
    private InfobipEmailService emailService;

    @Mock
    private GenreSuggester genreSuggester;

    @Mock
    private WatchlistGroupService watchlistGroupService;

    @Mock
    private WatchlistEntryRepository watchlistEntryRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMovieById() {
        Long movieId = 1L;
        Movie movie = new Movie();
        movie.setMovieId(movieId);
        movie.setTitle("Inception");

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        Optional<Movie> foundMovie = movieService.getMovieById(movieId);

        assertTrue(foundMovie.isPresent());
        assertEquals("Inception", foundMovie.get().getTitle());
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void testGetMovieById_NotFound() {
        Long movieId = 1L;

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        Optional<Movie> foundMovie = movieService.getMovieById(movieId);

        assertFalse(foundMovie.isPresent());
        verify(movieRepository, times(1)).findById(movieId);
    }


    @Test
    void testGetMoviesByUser() {
        User user = new User();
        user.setUserId(1L);

        Movie movie1 = new Movie();
        movie1.setTitle("Inception");
        Movie movie2 = new Movie();
        movie2.setTitle("The Dark Knight");

        when(movieRepository.findByUser(user)).thenReturn(List.of(movie1, movie2));

        List<Movie> movies = movieService.getMoviesByUser(user);

        assertEquals(2, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
        assertEquals("The Dark Knight", movies.get(1).getTitle());
    }

    @Test
    void testFilterMoviesByStatus() {
        Long userId = 1L;
        String status = "To Watch";

        Movie movie = new Movie();
        movie.setStatus(status);

        when(movieRepository.findByStatusAndUserId(userId, status)).thenReturn(List.of(movie));

        List<Movie> movies = movieService.filterMoviesByStatus(userId, status);

        assertEquals(1, movies.size());
        assertEquals("To Watch", movies.get(0).getStatus());
    }

    @Test
    void testFilterMoviesByGenre() {
        Long userId = 1L;
        String genreName = "Action";

        Genre genre = new Genre();
        genre.setGenreId(1L);
        genre.setName(genreName);

        Movie movie = new Movie();
        movie.setGenre(genre);

        when(genreRepository.findByName(genreName)).thenReturn(Optional.of(genre));
        when(movieRepository.findByGenreAndUserId(userId, genre.getGenreId())).thenReturn(List.of(movie));

        List<Movie> movies = movieService.filterMoviesByGenre(userId, genreName);

        assertEquals(1, movies.size());
        assertEquals("Action", movies.get(0).getGenre().getName());
    }

    @Test
    void testFilterMoviesByStatusToWatch() {

        Movie movie1 = new Movie();
        movie1.setTitle("Movie 1");
        movie1.setStatus("To Watch");

        Movie movie2 = new Movie();
        movie2.setTitle("Movie 2");
        movie2.setStatus("To Watch");

        Long userId = 1L;

        when(movieRepository.findByStatusAndUserId(userId, "To Watch")).thenReturn(List.of(movie1, movie2));

        List<Movie> movies = movieService.filterMoviesByStatus(userId, "To Watch");

        assertEquals(2, movies.size());
        assertEquals("Movie 1", movies.get(0).getTitle());
        assertEquals("Movie 2", movies.get(1).getTitle());
        assertEquals("To Watch", movies.get(0).getStatus());
        assertEquals("To Watch", movies.get(1).getStatus());

        verify(movieRepository, times(1)).findByStatusAndUserId(userId, "To Watch");
    }

    @Test
    void testSortMoviesByWatchlistOrderAsc() {
        Movie movie1 = new Movie();
        movie1.setWatchlistOrder("Next Up");

        Movie movie2 = new Movie();
        movie2.setWatchlistOrder("Someday");

        when(movieRepository.findAllByUserIdOrderByWatchlistOrderAsc(1L)).thenReturn(List.of(movie1, movie2));

        List<Movie> movies = movieService.sortMoviesByWatchlistOrder(1L, "asc");

        assertEquals(2, movies.size());
        assertEquals("Next Up", movies.get(0).getWatchlistOrder());
        assertEquals("Someday", movies.get(1).getWatchlistOrder());
        verify(movieRepository, times(1)).findAllByUserIdOrderByWatchlistOrderAsc(1L);
    }

    @Test
    void testSortMoviesByWatchlistOrderDesc() {
        Movie movie1 = new Movie();
        movie1.setWatchlistOrder("Next Up");

        Movie movie2 = new Movie();
        movie2.setWatchlistOrder("Someday");

        when(movieRepository.findAllByUserIdOrderByWatchlistOrderDesc(1L)).thenReturn(List.of(movie2, movie1));

        List<Movie> movies = movieService.sortMoviesByWatchlistOrder(1L, "desc");

        assertEquals(2, movies.size());
        assertEquals("Someday", movies.get(0).getWatchlistOrder());
        assertEquals("Next Up", movies.get(1).getWatchlistOrder());
        verify(movieRepository, times(1)).findAllByUserIdOrderByWatchlistOrderDesc(1L);
    }

    @Test
    void testSortMoviesByWatchlistOrderInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movieService.sortMoviesByWatchlistOrder(1L, "invalid");
        });

        assertEquals("Invalid order parameter. Use 'asc' or 'desc'.", exception.getMessage());
    }

    @Test
    void testGetMoviesByUserSortedByTitle() {
        User user = new User();
        user.setUserId(1L);

        WatchlistGroup watchlistGroup = new WatchlistGroup();
        watchlistGroup.setId(1L);
        watchlistGroup.setName("Favorites");

        WatchlistEntry entry1 = new WatchlistEntry();
        entry1.setWatchlistGroup(watchlistGroup);

        WatchlistEntry entry2 = new WatchlistEntry();
        entry2.setWatchlistGroup(watchlistGroup);

        Movie movie1 = new Movie();
        movie1.setTitle("A Movie");
        movie1.setUser(user);
        movie1.setWatchlistEntries(List.of(entry1));

        Movie movie2 = new Movie();
        movie2.setTitle("B Movie");
        movie2.setUser(user);
        movie2.setWatchlistEntries(List.of(entry2));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieRepository.findAllMoviesByUserSortedByTitle(user)).thenReturn(List.of(movie1, movie2));

        List<Movie> movies = movieService.getMoviesByUserSortedByTitle(1L);

        assertEquals(2, movies.size());
        assertEquals("A Movie", movies.get(0).getTitle());
        assertEquals("B Movie", movies.get(1).getTitle());
        assertEquals(1, movies.get(0).getWatchlistEntries().size());
        assertEquals("Favorites", movies.get(0).getWatchlistEntries().get(0).getWatchlistGroup().getName());

        verify(userRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).findAllMoviesByUserSortedByTitle(user);
    }

    @Test
    void testEditMovie_Simple() {
        Long movieId = 1L;

        // Create an existing movie
        Movie existingMovie = new Movie();
        existingMovie.setMovieId(movieId);
        existingMovie.setTitle("Old Title");
        existingMovie.setDescription("Old Description");
        existingMovie.setWatchlistOrder("Someday");

        Genre genre = new Genre();
        genre.setGenreId(1L);
        genre.setName("Action");
        existingMovie.setGenre(genre);

        // Create the updated movie DTO
        MovieDTO updatedMovieDTO = new MovieDTO(
                "New Title",
                "New Description",
                "To Watch",
                "Next Up",
                "Action",
                List.of("Favorites")
        );

        // Mock dependencies
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(genreService.getGenreByName("Action")).thenReturn(Optional.of(genre));
        when(movieRepository.save(any(Movie.class))).thenReturn(existingMovie);

        // Act: Call the service method
        Movie result = movieService.editMovie(movieId, updatedMovieDTO);

        // Assert: Verify that fields were updated correctly
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals("Next Up", result.getWatchlistOrder());
        assertEquals("Action", result.getGenre().getName());

        // Verify interactions with mocks
        verify(movieRepository, times(1)).findById(movieId);
        verify(genreService, times(1)).getGenreByName("Action");
        verify(movieRepository, times(1)).save(existingMovie);
    }






    @Test
    void testAddMovie() {
        Long userId = 1L;
        MovieDTO movieDTO = new MovieDTO("Inception", "A mind-bending thriller", "To Watch", "Next Up", "Action", List.of("Favorites"));

        Genre genre = new Genre();
        genre.setGenreId(1L);
        genre.setName("Action");

        User user = new User();
        user.setUserId(userId);

        WatchlistGroup watchlistGroup = new WatchlistGroup();
        watchlistGroup.setId(1L);
        watchlistGroup.setName("Favorites");

        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());

        when(genreService.getGenreByName(movieDTO.getGenreName())).thenReturn(Optional.of(genre));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(watchlistGroupService.createOrGetWatchlistGroup("Favorites")).thenReturn(watchlistGroup);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Movie savedMovie = movieService.addMovie(movieDTO, userId);

        assertEquals("Inception", savedMovie.getTitle());
        verify(watchlistEntryRepository, times(1)).save(any());
    }


    @Test
    void testDeleteMovie() {
        Long movieId = 1L;

        doNothing().when(movieRepository).deleteById(movieId);

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).deleteById(movieId);
    }


    @Test
    void testMarkAsWatched_Success() {
        Long userId = 1L;
        Long movieId = 2L;

        User user = new User();
        user.setUserId(userId);
        user.setEmailEnabled(true);

        Genre genre = new Genre();
        genre.setName("Action");

        Movie movie = new Movie();
        movie.setStatus("To Watch");
        movie.setUser(user);
        movie.setGenre(genre);
        movie.setTitle("Inception");

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);
        when(genreSuggester.suggestMovies(anyString())).thenReturn("Some suggested movies");

        movieService.markAsWatched(userId, movieId);

        assertEquals("Watched", movie.getStatus());
        verify(emailService, times(1)).sendEmail(
                eq(user.getEmail()),
                eq("Thank you for watching!"),
                contains("Dear user, thank you for watching \"Inception\".")
        );
    }

    @Test
    void testMarkAsWatched_AlreadyWatched() {
        Long movieId = 2L;

        Movie movie = new Movie();
        movie.setStatus("Watched");

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            movieService.markAsWatched(1L, movieId);
        });

        assertEquals("This movie has already been marked as watched", exception.getMessage());
    }
}
