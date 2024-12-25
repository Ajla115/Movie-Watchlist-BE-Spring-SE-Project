package ba.edu.ibu.movieswatchlist.core.repository;

import ba.edu.ibu.movieswatchlist.core.model.Movie;
import ba.edu.ibu.movieswatchlist.core.model.Genre;
import ba.edu.ibu.movieswatchlist.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Show all movies for a user
    List<Movie> findByUser(User user);

    // Filter by status for a specific user
    @Query("SELECT m FROM movies m WHERE m.user.userId = :userId AND m.status = :status")
    List<Movie> findByStatusAndUserId(@Param("userId") Long userId, @Param("status") String status);

    // Filter by watchlist order for a specific user
    @Query("SELECT m FROM movies m WHERE m.user.userId = :userId AND m.watchlistOrder = :watchlistOrder")
    List<Movie> findByWatchlistOrderAndUserId(@Param("userId") Long userId, @Param("watchlistOrder") String watchlistOrder);

    // Filter by genre for a specific user
    @Query("SELECT m FROM movies m WHERE m.user.userId = :userId AND m.genre.genreId = :genreId")
    List<Movie> findByGenreAndUserId(@Param("userId") Long userId, @Param("genreId") Long genreId);

    // Find all movies sorted by title for a user
    @Query("SELECT m FROM movies m WHERE m.user = :user ORDER BY m.title ASC")
    List<Movie> findAllMoviesByUserSortedByTitle(@Param("user") User user);

    @Query("SELECT m FROM movies m WHERE m.user.userId = :userId ORDER BY m.watchlistOrder ASC")
    List<Movie> findAllByUserIdOrderByWatchlistOrderAsc(@Param("userId") Long userId);

    @Query("SELECT m FROM movies m WHERE m.user.userId = :userId ORDER BY m.watchlistOrder DESC")
    List<Movie> findAllByUserIdOrderByWatchlistOrderDesc(@Param("userId") Long userId);


}

