package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.core.model.WatchlistGroup;
import ba.edu.ibu.movieswatchlist.core.repository.WatchlistEntryRepository;
import ba.edu.ibu.movieswatchlist.core.repository.WatchlistGroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchlistGroupService {

    private final WatchlistGroupRepository watchlistGroupRepository;
    private final WatchlistEntryRepository watchlistEntryRepository;

    public WatchlistGroupService(WatchlistGroupRepository watchlistGroupRepository, WatchlistEntryRepository watchlistEntryRepository) {
        this.watchlistGroupRepository = watchlistGroupRepository;
        this.watchlistEntryRepository = watchlistEntryRepository;
    }


    public WatchlistGroup createOrGetWatchlistGroup(String name) {
        Optional<WatchlistGroup> existingGroup = watchlistGroupRepository.findByNameIgnoreCase(name);
        if (existingGroup.isPresent()) {
            return existingGroup.get();
        }
        WatchlistGroup newGroup = new WatchlistGroup();
        newGroup.setName(name);
        return watchlistGroupRepository.save(newGroup);
    }

    public WatchlistGroup createWatchlistGroup(String name) {
        Optional<WatchlistGroup> existingGroup = watchlistGroupRepository.findByNameIgnoreCase(name);

        if (existingGroup.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A watchlist group with the name \"" + name + "\" already exists."
            );
        }
        WatchlistGroup newGroup = new WatchlistGroup();
        newGroup.setName(name);
        return watchlistGroupRepository.save(newGroup);
    }


    public WatchlistGroup renameWatchlistGroup(Long groupId, String newName) {
        WatchlistGroup group = watchlistGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Watchlist group not found"));
        group.setName(newName);
        return watchlistGroupRepository.save(group);
    }

    @Transactional
    public void deleteWatchlistGroup(Long groupId) {
        if (!watchlistGroupRepository.existsById(groupId)) {
            throw new IllegalArgumentException("Watchlist group not found");
        }
        watchlistEntryRepository.deleteByWatchlistGroupId(groupId);
        watchlistGroupRepository.deleteById(groupId);
    }

    public List<WatchlistGroup> getAllWatchlistGroups() {
        return watchlistGroupRepository.findAll();
    }

    public List<WatchlistGroup> getAllWatchlistGroupsIdsAndNames() {
        return watchlistGroupRepository.findAll().stream()
                .map(group -> {
                    WatchlistGroup minimalGroup = new WatchlistGroup();
                    minimalGroup.setId(group.getId());
                    minimalGroup.setName(group.getName());
                    return minimalGroup;
                })
                .collect(Collectors.toList());
    }
}
