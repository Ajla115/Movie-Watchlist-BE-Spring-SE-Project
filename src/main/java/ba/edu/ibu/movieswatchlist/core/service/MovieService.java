package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.core.model.Genre;
import ba.edu.ibu.movieswatchlist.core.model.Movie;
import ba.edu.ibu.movieswatchlist.core.model.User;
import ba.edu.ibu.movieswatchlist.core.repository.GenreRepository;
import ba.edu.ibu.movieswatchlist.core.repository.MovieRepository;
import ba.edu.ibu.movieswatchlist.core.repository.UserRepository;
import ba.edu.ibu.movieswatchlist.rest.dto.MovieDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final GenreService genreService;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository movieRepository, UserRepository userRepository, GenreService genreService, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.genreService = genreService;
        this.genreRepository = genreRepository;
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

    public List<Movie> filterMoviesByGenre(Long userId, String genreName) {
        Genre genre = genreRepository.findByName(genreName)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + genreName));
        return movieRepository.findByGenreAndUserId(userId, genre.getGenreId());
    }


    public List<Movie> getMoviesByUserSortedByTitle(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return movieRepository.findAllMoviesByUserSortedByTitle(user);
    }

    public List<Movie> sortMoviesByWatchlistOrder(Long userId, String order) {
        if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Invalid order parameter. Use 'asc' or 'desc'.");
        }

        if (order.equalsIgnoreCase("asc")) {
            return movieRepository.findAllByUserIdOrderByWatchlistOrderAsc(userId);
        } else {
            return movieRepository.findAllByUserIdOrderByWatchlistOrderDesc(userId);
        }
    }

    public Movie addMovie(MovieDTO movieDTO, Long userId) {
        // Create a new movie object
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setStatus(movieDTO.getStatus());
        movie.setWatchlistOrder(movieDTO.getWatchlistOrder());

        // Fetch the genre by its name
        Genre genre = genreService.getGenreByName(movieDTO.getGenreName())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + movieDTO.getGenreName()));
        movie.setGenre(genre);

        // Fetch the user by its ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: ID " + userId));
        movie.setUser(user);

        // Save and return the movie
        return movieRepository.save(movie);
    }

    public Optional<Movie> getMovieById(Long movieId) {
        return movieRepository.findById(movieId);
    }

    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }

    public Movie editMovie(Long movieId, MovieDTO updatedMovieDTO) {
        // Fetch the existing movie
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        // Update the movie's fields
        if (updatedMovieDTO.getTitle() != null) {
            existingMovie.setTitle(updatedMovieDTO.getTitle());
        }

        if (updatedMovieDTO.getDescription() != null) {
            existingMovie.setDescription(updatedMovieDTO.getDescription());
        }

        if (updatedMovieDTO.getWatchlistOrder() != null) {
            existingMovie.setWatchlistOrder(updatedMovieDTO.getWatchlistOrder());
        }

        if (updatedMovieDTO.getStatus() != null) {
            existingMovie.setStatus(updatedMovieDTO.getStatus());
        }

        if (updatedMovieDTO.getGenreName() != null) {
            Genre genre = genreRepository.findByName(updatedMovieDTO.getGenreName())
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + updatedMovieDTO.getGenreName()));
            existingMovie.setGenre(genre);
        }

        // Save and return the updated movie
        return movieRepository.save(existingMovie);
    }


}
