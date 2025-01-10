package ba.edu.ibu.movieswatchlist.rest.controllers;

import ba.edu.ibu.movieswatchlist.core.model.Movie;
import ba.edu.ibu.movieswatchlist.core.service.GenreService;
import ba.edu.ibu.movieswatchlist.core.service.MovieService;
import ba.edu.ibu.movieswatchlist.core.service.WatchlistGroupService;
import ba.edu.ibu.movieswatchlist.rest.dto.MovieDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final GenreService genreService;
    private final WatchlistGroupService watchlistGroupService;


    public MovieController(MovieService movieService, GenreService genreService, WatchlistGroupService  watchlistGroupService) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.watchlistGroupService = watchlistGroupService;
    }

    @PostMapping("/add/user/{userId}")
    public ResponseEntity<Movie> createMovie(@RequestBody MovieDTO movieDTO, @PathVariable Long userId) {
        return ResponseEntity.ok(movieService.addMovie(movieDTO, userId));
    }


    @GetMapping("/get-all/user/{userId}")
    public ResponseEntity<List<Movie>> getMoviesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(movieService.getMoviesByUserSortedByTitle(userId));
    }

    @GetMapping("/sort/watchlist/user/{userId}")
    public ResponseEntity<List<Movie>> sortMoviesByWatchlistOrder(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(movieService.sortMoviesByWatchlistOrder(userId, order));
    }


    @GetMapping("/filter/status/user/{userId}")
    public ResponseEntity<List<Movie>> filterMoviesByStatus(
            @PathVariable Long userId,
            @RequestParam String status) {
        return ResponseEntity.ok(movieService.filterMoviesByStatus(userId, status));
    }


    @GetMapping("/filter/watchlist/user/{userId}")
    public ResponseEntity<List<Movie>> filterMoviesByWatchlistOrder(
            @PathVariable Long userId,
            @RequestParam String order) {
        return ResponseEntity.ok(movieService.filterMoviesByWatchlistOrder(userId, order));
    }


    @GetMapping("/filter/genre/user/{userId}")
    public ResponseEntity<List<Movie>> filterMoviesByGenre(
            @PathVariable Long userId,
            @RequestParam String genreName) {
        return ResponseEntity.ok(movieService.filterMoviesByGenre(userId, genreName));
    }

    @PutMapping("/edit/{movieId}")
    public ResponseEntity<Movie> editMovie(
            @PathVariable Long movieId,
            @RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(movieService.editMovie(movieId, movieDTO));
    }

    @PutMapping("/mark-watched/{userId}/{movieId}")
    public ResponseEntity<?> markMovieAsWatched(@PathVariable Long userId, @PathVariable Long movieId) {
        try {
            Movie updatedMovie = movieService.markAsWatched(userId, movieId);
            return ResponseEntity.ok(updatedMovie);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }







}