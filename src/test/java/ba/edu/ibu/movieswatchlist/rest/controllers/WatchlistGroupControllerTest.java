package ba.edu.ibu.movieswatchlist.rest.controllers;

import ba.edu.ibu.movieswatchlist.core.model.Movie;
import ba.edu.ibu.movieswatchlist.core.model.WatchlistGroup;
import ba.edu.ibu.movieswatchlist.core.service.MovieService;
import ba.edu.ibu.movieswatchlist.core.service.WatchlistGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WatchlistGroupControllerTest {

    @Mock
    private WatchlistGroupService watchlistGroupService;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private WatchlistGroupController watchlistGroupController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(watchlistGroupController).build();
    }

    @Test
    void testCreateOrGetWatchlistGroup() throws Exception {
        WatchlistGroup group = new WatchlistGroup();
        group.setId(1L);
        group.setName("Favorites");

        when(watchlistGroupService.createOrGetWatchlistGroup("Favorites")).thenReturn(group);

        mockMvc.perform(post("/api/watchlists/add-indirectly")
                        .param("name", "Favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Favorites"));

        verify(watchlistGroupService, times(1)).createOrGetWatchlistGroup("Favorites");
    }

    @Test
    void testCreateWatchlistGroup() throws Exception {
        WatchlistGroup group = new WatchlistGroup();
        group.setId(2L);
        group.setName("New Group");

        when(watchlistGroupService.createWatchlistGroup("New Group")).thenReturn(group);

        mockMvc.perform(post("/api/watchlists/add-directly")
                        .param("name", "New Group"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("New Group"));

        verify(watchlistGroupService, times(1)).createWatchlistGroup("New Group");
    }

    @Test
    void testRenameWatchlistGroup() throws Exception {
        WatchlistGroup group = new WatchlistGroup();
        group.setId(1L);
        group.setName("Renamed Group");

        when(watchlistGroupService.renameWatchlistGroup(1L, "Renamed Group")).thenReturn(group);

        mockMvc.perform(put("/api/watchlists/edit/1")
                        .param("newName", "Renamed Group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Renamed Group"));

        verify(watchlistGroupService, times(1)).renameWatchlistGroup(1L, "Renamed Group");
    }

    @Test
    void testDeleteWatchlistGroup_DeleteMovies() throws Exception {
        doNothing().when(movieService).deleteGroupAndMovies(1L);

        mockMvc.perform(delete("/api/watchlists/delete/1")
                        .param("deleteMovies", "true"))
                .andExpect(status().isNoContent());

        verify(movieService, times(1)).deleteGroupAndMovies(1L);
        verify(movieService, never()).deleteGroupOnly(anyLong());
    }

    @Test
    void testDeleteWatchlistGroup_KeepMovies() throws Exception {
        doNothing().when(movieService).deleteGroupOnly(1L);

        mockMvc.perform(delete("/api/watchlists/delete/1")
                        .param("deleteMovies", "false"))
                .andExpect(status().isNoContent());

        verify(movieService, times(1)).deleteGroupOnly(1L);
        verify(movieService, never()).deleteGroupAndMovies(anyLong());
    }

    @Test
    void testGetAllWatchlistGroups() throws Exception {
        WatchlistGroup group1 = new WatchlistGroup();
        group1.setId(1L);
        group1.setName("Group 1");

        WatchlistGroup group2 = new WatchlistGroup();
        group2.setId(2L);
        group2.setName("Group 2");

        when(watchlistGroupService.getAllWatchlistGroupsIdsAndNames()).thenReturn(List.of(group1, group2));

        mockMvc.perform(get("/api/watchlists/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Group 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Group 2"));

        verify(watchlistGroupService, times(1)).getAllWatchlistGroupsIdsAndNames();
    }

    @Test
    void testGetMoviesByWatchlistGroup() throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovieId(1L);
        movie1.setTitle("Movie 1");

        Movie movie2 = new Movie();
        movie2.setMovieId(2L);
        movie2.setTitle("Movie 2");

        when(movieService.getMoviesByWatchlistGroupAndUser(1L, 1L)).thenReturn(List.of(movie1, movie2));

        mockMvc.perform(get("/api/watchlists/movies-by-group/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Movie 1"))
                .andExpect(jsonPath("$[1].movieId").value(2L))
                .andExpect(jsonPath("$[1].title").value("Movie 2"));

        verify(movieService, times(1)).getMoviesByWatchlistGroupAndUser(1L, 1L);
    }
}
