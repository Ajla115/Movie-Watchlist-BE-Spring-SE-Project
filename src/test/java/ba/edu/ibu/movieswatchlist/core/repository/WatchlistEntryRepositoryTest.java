package ba.edu.ibu.movieswatchlist.core.repository;

import ba.edu.ibu.movieswatchlist.core.model.WatchlistEntry;
import ba.edu.ibu.movieswatchlist.core.model.WatchlistGroup;
import ba.edu.ibu.movieswatchlist.core.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WatchlistEntryRepositoryTest {

    @Mock
    private WatchlistEntryRepository watchlistEntryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByMovieMovieId() {
        Long movieId = 1L;
        Movie movie = new Movie();
        movie.setMovieId(movieId);

        WatchlistGroup group1 = new WatchlistGroup();
        group1.setName("Favorites");

        WatchlistEntry entry1 = new WatchlistEntry(movie, group1);

        when(watchlistEntryRepository.findByMovieMovieId(movieId)).thenReturn(List.of(entry1));

        List<WatchlistEntry> entries = watchlistEntryRepository.findByMovieMovieId(movieId);

        assertEquals(1, entries.size(), "There should be one entry");
        assertEquals("Favorites", entries.get(0).getWatchlistGroup().getName(), "Group name should match");
    }

    @Test
    void testFindByWatchlistGroupId() {
        Long groupId = 1L;
        WatchlistGroup group = new WatchlistGroup();
        group.setId(groupId);

        Movie movie = new Movie();
        movie.setTitle("Inception");

        WatchlistEntry entry = new WatchlistEntry(movie, group);

        when(watchlistEntryRepository.findByWatchlistGroupId(groupId)).thenReturn(List.of(entry));

        List<WatchlistEntry> entries = watchlistEntryRepository.findByWatchlistGroupId(groupId);

        assertEquals(1, entries.size(), "There should be one entry");
        assertEquals("Inception", entries.get(0).getMovie().getTitle(), "Movie title should match");
    }

    @Test
    void testDeleteByWatchlistGroupId() {
        Long groupId = 1L;

        doNothing().when(watchlistEntryRepository).deleteByWatchlistGroupId(groupId);

        watchlistEntryRepository.deleteByWatchlistGroupId(groupId);

        verify(watchlistEntryRepository, times(1)).deleteByWatchlistGroupId(groupId);
    }

    @Test
    void testDeleteByMovieMovieId() {
        Long movieId = 1L;

        doNothing().when(watchlistEntryRepository).deleteByMovieMovieId(movieId);

        watchlistEntryRepository.deleteByMovieMovieId(movieId);

        verify(watchlistEntryRepository, times(1)).deleteByMovieMovieId(movieId);
    }
}