package ba.edu.ibu.movieswatchlist.rest.controllers;

import ba.edu.ibu.movieswatchlist.core.model.Genre;
import ba.edu.ibu.movieswatchlist.core.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping("/add")
    public ResponseEntity<Genre> addGenre(@RequestBody Genre genre) {
        return ResponseEntity.ok(genreService.addGenre(genre));
    }

    @GetMapping("/listallgenres")
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

 @GetMapping("/suggest/{title}")
    public ResponseEntity<String> suggestGenre(@PathVariable String title) {
        String genre = genreService.suggestGenre(title);
        return ResponseEntity.ok(genre);
    }
}