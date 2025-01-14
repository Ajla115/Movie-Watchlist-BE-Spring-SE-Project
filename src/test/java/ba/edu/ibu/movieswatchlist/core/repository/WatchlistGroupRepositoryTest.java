package ba.edu.ibu.movieswatchlist.core.repository;

import static org.junit.jupiter.api.Assertions.*;
import ba.edu.ibu.movieswatchlist.core.model.WatchlistGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WatchlistGroupRepositoryTest {

    @Mock
    private WatchlistGroupRepository watchlistGroupRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByNameIgnoreCase_GroupExists() {
        // Arrange
        String groupName = "Favorites";
        WatchlistGroup mockGroup = new WatchlistGroup();
        mockGroup.setId(1L);
        mockGroup.setName(groupName);

        when(watchlistGroupRepository.findByNameIgnoreCase(groupName)).thenReturn(Optional.of(mockGroup));

        // Act
        Optional<WatchlistGroup> foundGroup = watchlistGroupRepository.findByNameIgnoreCase(groupName);

        // Assert
        assertTrue(foundGroup.isPresent(), "Watchlist group should be found");
        assertEquals(groupName, foundGroup.get().getName(), "Group name should match");
    }

    @Test
    void testFindByNameIgnoreCase_GroupDoesNotExist() {
        // Arrange
        String groupName = "NonExistentGroup";

        when(watchlistGroupRepository.findByNameIgnoreCase(groupName)).thenReturn(Optional.empty());

        // Act
        Optional<WatchlistGroup> foundGroup = watchlistGroupRepository.findByNameIgnoreCase(groupName);

        // Assert
        assertFalse(foundGroup.isPresent(), "Watchlist group should not be found");
    }
}
