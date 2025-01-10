package ba.edu.ibu.movieswatchlist.core.repository;

import ba.edu.ibu.movieswatchlist.core.model.WatchlistGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchlistGroupRepository extends JpaRepository<WatchlistGroup, Long> {
    Optional<WatchlistGroup> findByNameIgnoreCase(String name);

}
