package ba.edu.ibu.movieswatchlist.core.repository;

import ba.edu.ibu.movieswatchlist.core.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    // Find genre by name
    Optional<Genre> findByName(String name);

    // Check if a genre exists by name
    boolean existsByName(String name);

    // Fetch all genres sorted alphabetically
    @Query("SELECT g FROM genres g ORDER BY g.name ASC")
    List<Genre> findAllSortedByName();
}
