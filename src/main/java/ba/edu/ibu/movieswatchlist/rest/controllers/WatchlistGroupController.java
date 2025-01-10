package ba.edu.ibu.movieswatchlist.rest.controllers;


import ba.edu.ibu.movieswatchlist.core.model.Movie;
import ba.edu.ibu.movieswatchlist.core.model.WatchlistGroup;
import ba.edu.ibu.movieswatchlist.core.service.MovieService;
import ba.edu.ibu.movieswatchlist.core.service.WatchlistGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlists")
public class WatchlistGroupController {
    private final WatchlistGroupService watchlistGroupService;
    private final MovieService movieService;

    public WatchlistGroupController(WatchlistGroupService watchlistGroupService, MovieService movieService) {
        this.movieService = movieService;
        this.watchlistGroupService = watchlistGroupService;
    }

    @PostMapping("/add")
    public ResponseEntity<WatchlistGroup> createWatchlistGroup(@RequestParam String name) {
        return ResponseEntity.ok(watchlistGroupService.createOrGetWatchlistGroup(name));
    }

    @PutMapping("/edit/{groupId}")
    public ResponseEntity<WatchlistGroup> renameWatchlistGroup(@PathVariable Long groupId, @RequestParam String newName) {
        return ResponseEntity.ok(watchlistGroupService.renameWatchlistGroup(groupId, newName));
    }

    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<Void> deleteWatchlistGroup(@PathVariable Long groupId, @RequestParam boolean deleteMovies) {
        if (deleteMovies) {
            movieService.deleteGroupAndMovies(groupId);
        } else {
            movieService.deleteGroupOnly(groupId);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<WatchlistGroup>> getAllWatchlistGroups() {
        return ResponseEntity.ok(watchlistGroupService.getAllWatchlistGroupsIdsAndNames());
    }

    @GetMapping("/movies-by-group")
    public ResponseEntity<List<Movie>> getMoviesByWatchlistGroup(@RequestParam Long groupId, @RequestParam Long userId) {
        return ResponseEntity.ok(movieService.getMoviesByWatchlistGroupAndUser(groupId, userId));
    }
}

