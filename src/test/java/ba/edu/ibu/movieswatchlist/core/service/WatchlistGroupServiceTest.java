package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.core.model.WatchlistGroup;
import ba.edu.ibu.movieswatchlist.core.repository.WatchlistEntryRepository;
import ba.edu.ibu.movieswatchlist.core.repository.WatchlistGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WatchlistGroupServiceTest {

    @Mock
    private WatchlistGroupRepository watchlistGroupRepository;

    @Mock
    private WatchlistEntryRepository watchlistEntryRepository;

    @InjectMocks
    private WatchlistGroupService watchlistGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrGetWatchlistGroup_GroupExists() {
        String groupName = "Favorites";
        WatchlistGroup existingGroup = new WatchlistGroup();
        existingGroup.setId(1L);
        existingGroup.setName(groupName);

        when(watchlistGroupRepository.findByNameIgnoreCase(groupName)).thenReturn(Optional.of(existingGroup));

        WatchlistGroup result = watchlistGroupService.createOrGetWatchlistGroup(groupName);

        assertEquals(1L, result.getId());
        assertEquals("Favorites", result.getName());
        verify(watchlistGroupRepository, times(1)).findByNameIgnoreCase(groupName);
        verify(watchlistGroupRepository, never()).save(any());
    }

    @Test
    void testCreateOrGetWatchlistGroup_GroupDoesNotExist() {
        String groupName = "New Group";
        WatchlistGroup newGroup = new WatchlistGroup();
        newGroup.setId(2L);
        newGroup.setName(groupName);

        when(watchlistGroupRepository.findByNameIgnoreCase(groupName)).thenReturn(Optional.empty());
        when(watchlistGroupRepository.save(any(WatchlistGroup.class))).thenReturn(newGroup);

        WatchlistGroup result = watchlistGroupService.createOrGetWatchlistGroup(groupName);

        assertEquals(2L, result.getId());
        assertEquals("New Group", result.getName());
        verify(watchlistGroupRepository, times(1)).findByNameIgnoreCase(groupName);
        verify(watchlistGroupRepository, times(1)).save(any(WatchlistGroup.class));
    }

    @Test
    void testCreateWatchlistGroup_Success() {
        String groupName = "Unique Group";

        when(watchlistGroupRepository.findByNameIgnoreCase(groupName)).thenReturn(Optional.empty());

        WatchlistGroup newGroup = new WatchlistGroup();
        newGroup.setId(3L);
        newGroup.setName(groupName);

        when(watchlistGroupRepository.save(any(WatchlistGroup.class))).thenReturn(newGroup);

        WatchlistGroup result = watchlistGroupService.createWatchlistGroup(groupName);

        assertEquals(3L, result.getId());
        assertEquals("Unique Group", result.getName());
        verify(watchlistGroupRepository, times(1)).findByNameIgnoreCase(groupName);
        verify(watchlistGroupRepository, times(1)).save(any(WatchlistGroup.class));
    }

    @Test
    void testRenameWatchlistGroup_Success() {
        Long groupId = 1L;
        String newName = "Renamed Group";

        WatchlistGroup existingGroup = new WatchlistGroup();
        existingGroup.setId(groupId);
        existingGroup.setName("Old Group");

        when(watchlistGroupRepository.findById(groupId)).thenReturn(Optional.of(existingGroup));
        when(watchlistGroupRepository.save(existingGroup)).thenReturn(existingGroup);

        WatchlistGroup result = watchlistGroupService.renameWatchlistGroup(groupId, newName);

        assertEquals(groupId, result.getId());
        assertEquals("Renamed Group", result.getName());
        verify(watchlistGroupRepository, times(1)).findById(groupId);
        verify(watchlistGroupRepository, times(1)).save(existingGroup);
    }

    @Test
    void testRenameWatchlistGroup_NotFound() {
        Long groupId = 99L;
        String newName = "New Name";

        when(watchlistGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                watchlistGroupService.renameWatchlistGroup(groupId, newName)
        );

        assertEquals("Watchlist group not found", exception.getMessage());
        verify(watchlistGroupRepository, times(1)).findById(groupId);
        verify(watchlistGroupRepository, never()).save(any());
    }

    @Test
    void testDeleteWatchlistGroup_Success() {
        Long groupId = 1L;

        when(watchlistGroupRepository.existsById(groupId)).thenReturn(true);

        watchlistGroupService.deleteWatchlistGroup(groupId);

        verify(watchlistEntryRepository, times(1)).deleteByWatchlistGroupId(groupId);
        verify(watchlistGroupRepository, times(1)).deleteById(groupId);
    }

    @Test
    void testDeleteWatchlistGroup_NotFound() {
        Long groupId = 99L;

        when(watchlistGroupRepository.existsById(groupId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                watchlistGroupService.deleteWatchlistGroup(groupId)
        );

        assertEquals("Watchlist group not found", exception.getMessage());
        verify(watchlistEntryRepository, never()).deleteByWatchlistGroupId(anyLong());
        verify(watchlistGroupRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetAllWatchlistGroups() {
        WatchlistGroup group1 = new WatchlistGroup();
        group1.setId(1L);
        group1.setName("Group 1");

        WatchlistGroup group2 = new WatchlistGroup();
        group2.setId(2L);
        group2.setName("Group 2");

        when(watchlistGroupRepository.findAll()).thenReturn(List.of(group1, group2));

        List<WatchlistGroup> result = watchlistGroupService.getAllWatchlistGroups();

        assertEquals(2, result.size());
        assertEquals("Group 1", result.get(0).getName());
        assertEquals("Group 2", result.get(1).getName());
        verify(watchlistGroupRepository, times(1)).findAll();
    }

    @Test
    void testGetAllWatchlistGroupsIdsAndNames() {
        WatchlistGroup group1 = new WatchlistGroup();
        group1.setId(1L);
        group1.setName("Group 1");

        WatchlistGroup group2 = new WatchlistGroup();
        group2.setId(2L);
        group2.setName("Group 2");

        when(watchlistGroupRepository.findAll()).thenReturn(List.of(group1, group2));

        List<WatchlistGroup> result = watchlistGroupService.getAllWatchlistGroupsIdsAndNames();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Group 1", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Group 2", result.get(1).getName());
        verify(watchlistGroupRepository, times(1)).findAll();
    }
}
