package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.core.model.Movie;
import ba.edu.ibu.movieswatchlist.core.model.User;
import ba.edu.ibu.movieswatchlist.core.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getMoviesByUser(User user) {
        return movieRepository.findByUser(user);
    }

    public List<Movie> filterMoviesByStatus(Long userId, String status) {
        return movieRepository.findByStatusAndUserId(userId, status);
    }

    public List<Movie> filterMoviesByWatchlistOrder(Long userId, String watchlistOrder) {
        return movieRepository.findByWatchlistOrderAndUserId(userId, watchlistOrder);
    }

    public List<Movie> filterMoviesByGenre(Long userId, Long genreId) {
        return movieRepository.findByGenreAndUserId(userId, genreId);
    }

    public List<Movie> sortMoviesByTitle(User user) {
        return movieRepository.findAllMoviesByUserSortedByTitle(user);
    }

    public List<Movie> sortMoviesByWatchlistOrderAsc(Long userId) {
        return movieRepository.findAllByUserIdOrderByWatchlistOrderAsc(userId);
    }

    public List<Movie> sortMoviesByWatchlistOrderDesc(Long userId) {
        return movieRepository.findAllByUserIdOrderByWatchlistOrderDesc(userId);
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Optional<Movie> getMovieById(Long movieId) {
        return movieRepository.findById(movieId);
    }

    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }

    public Movie editMovie(Long movieId, Movie updatedMovie) {
        // Fetch the existing movie
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        // Update fields with new values from the updated movie object
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setWatchlistOrder(updatedMovie.getWatchlistOrder());
        existingMovie.setStatus(updatedMovie.getStatus());
        existingMovie.setGenre(updatedMovie.getGenre());

        // Save and return the updated movie
        return movieRepository.save(existingMovie);
    }
}
