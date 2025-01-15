package ba.edu.ibu.movieswatchlist.rest.controllers;

import ba.edu.ibu.movieswatchlist.core.model.Movie;
import ba.edu.ibu.movieswatchlist.core.service.GenreService;
import ba.edu.ibu.movieswatchlist.core.service.MovieService;
import ba.edu.ibu.movieswatchlist.rest.dto.MovieDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    void testCreateMovie() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Inception");
        Movie movie = new Movie();
        movie.setTitle("Inception");

        when(movieService.addMovie(any(MovieDTO.class), eq(1L))).thenReturn(movie);

        mockMvc.perform(post("/api/movies/add/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Inception\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));

        verify(movieService, times(1)).addMovie(any(MovieDTO.class), eq(1L));
    }

    @Test
    void testGetMoviesByUser() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");

        when(movieService.getMoviesByUserSortedByTitle(1L)).thenReturn(List.of(movie));

        mockMvc.perform(get("/api/movies/get-all/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"));

        verify(movieService, times(1)).getMoviesByUserSortedByTitle(1L);
    }

    @Test
    void testSortMoviesByWatchlistOrder() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");

        when(movieService.sortMoviesByWatchlistOrder(1L, "asc")).thenReturn(List.of(movie));

        mockMvc.perform(get("/api/movies/sort/watchlist/user/1")
                        .param("order", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"));

        verify(movieService, times(1)).sortMoviesByWatchlistOrder(1L, "asc");
    }

    @Test
    void testFilterMoviesByStatus() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");

        when(movieService.filterMoviesByStatus(1L, "To Watch")).thenReturn(List.of(movie));

        mockMvc.perform(get("/api/movies/filter/status/user/1")
                        .param("status", "To Watch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"));

        verify(movieService, times(1)).filterMoviesByStatus(1L, "To Watch");
    }

    @Test
    void testFilterMoviesByWatchlistOrder() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");

        when(movieService.filterMoviesByWatchlistOrder(1L, "Next Up")).thenReturn(List.of(movie));

        mockMvc.perform(get("/api/movies/filter/watchlist/user/1")
                        .param("order", "Next Up"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"));

        verify(movieService, times(1)).filterMoviesByWatchlistOrder(1L, "Next Up");
    }

    @Test
    void testFilterMoviesByGenre() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");

        when(movieService.filterMoviesByGenre(1L, "Action")).thenReturn(List.of(movie));

        mockMvc.perform(get("/api/movies/filter/genre/user/1")
                        .param("genreName", "Action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"));

        verify(movieService, times(1)).filterMoviesByGenre(1L, "Action");
    }

    @Test
    void testEditMovie() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Inception Updated");
        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("Inception Updated");

        when(movieService.editMovie(eq(1L), any(MovieDTO.class))).thenReturn(updatedMovie);

        mockMvc.perform(put("/api/movies/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Inception Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception Updated"));

        verify(movieService, times(1)).editMovie(eq(1L), any(MovieDTO.class));
    }

    @Test
    void testMarkMovieAsWatched() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setStatus("Watched");

        when(movieService.markAsWatched(1L, 2L)).thenReturn(movie);

        mockMvc.perform(put("/api/movies/mark-watched/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Watched"));

        verify(movieService, times(1)).markAsWatched(1L, 2L);
    }

    @Test
    void testDeleteMovie() throws Exception {
        mockMvc.perform(delete("/api/movies/delete/29"))
                .andExpect(status().isNoContent());

        verify(movieService, times(1)).deleteMovie(29L);
    }
}
