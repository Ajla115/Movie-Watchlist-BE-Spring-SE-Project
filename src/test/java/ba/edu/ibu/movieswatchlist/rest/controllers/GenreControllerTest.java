package ba.edu.ibu.movieswatchlist.rest.controllers;
import ba.edu.ibu.movieswatchlist.core.model.Genre;
import ba.edu.ibu.movieswatchlist.core.service.GenreService;
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

class GenreControllerTest {

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
    }

    @Test
    void testAddGenre() throws Exception {
        Genre genre = new Genre();
        genre.setName("Action");

        when(genreService.addGenre(any(Genre.class))).thenReturn(genre);

        mockMvc.perform(post("/api/genres/add")
                        .content("{\"name\": \"Action\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Action"));

        verify(genreService, times(1)).addGenre(any(Genre.class));
    }

    @Test
    void testGetAllGenres() throws Exception {
        Genre genre1 = new Genre();
        genre1.setName("Action");
        Genre genre2 = new Genre();
        genre2.setName("Comedy");

        when(genreService.getAllGenres()).thenReturn(List.of(genre1, genre2));

        mockMvc.perform(get("/api/genres/listallgenres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Action"))
                .andExpect(jsonPath("$[1].name").value("Comedy"));

        verify(genreService, times(1)).getAllGenres();
    }

    @Test
    void testSuggestGenre() throws Exception {
        String movieTitle = "Inception";
        String suggestedGenre = "Science Fiction";

        when(genreService.suggestGenre(movieTitle)).thenReturn(suggestedGenre);

        mockMvc.perform(get("/api/genres/suggest/{title}", movieTitle))
                .andExpect(status().isOk())
                .andExpect(content().string(suggestedGenre));

        verify(genreService, times(1)).suggestGenre(movieTitle);
    }
}
