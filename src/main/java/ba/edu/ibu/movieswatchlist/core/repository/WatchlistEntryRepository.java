package ba.edu.ibu.movieswatchlist.core.repository;

import ba.edu.ibu.movieswatchlist.core.model.WatchlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistEntryRepository extends JpaRepository<WatchlistEntry, Long> {
    List<WatchlistEntry> findByMovieMovieId(Long movieId);
    List<WatchlistEntry> findByWatchlistGroupId(Long watchlistGroupId);
    void deleteByWatchlistGroupId(Long watchlistGroupId);
    void deleteByMovieMovieId(Long movieId);

}