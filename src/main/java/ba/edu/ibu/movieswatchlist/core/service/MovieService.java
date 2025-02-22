package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.api.impl.infobip.InfobipEmailService;
import ba.edu.ibu.movieswatchlist.core.api.genresuggester.GenreSuggester;
import ba.edu.ibu.movieswatchlist.core.model.*;
import ba.edu.ibu.movieswatchlist.core.repository.GenreRepository;
import ba.edu.ibu.movieswatchlist.core.repository.MovieRepository;
import ba.edu.ibu.movieswatchlist.core.repository.UserRepository;
import ba.edu.ibu.movieswatchlist.core.repository.WatchlistEntryRepository;
import ba.edu.ibu.movieswatchlist.rest.dto.MovieDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final GenreService genreService;
    private final GenreRepository genreRepository;
    private final InfobipEmailService emailService;
    private final GenreSuggester genreSuggester;
    private final WatchlistGroupService watchlistGroupService;
    private final WatchlistEntryRepository watchlistEntryRepository;

    public MovieService(MovieRepository movieRepository, UserRepository userRepository,
                        GenreService genreService, GenreRepository genreRepository,
                        InfobipEmailService emailService, GenreSuggester genreSuggester,
                        WatchlistGroupService watchlistGroupService,
                        WatchlistEntryRepository  watchlistEntryRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.genreService = genreService;
        this.genreRepository = genreRepository;
        this.emailService = emailService;
        this.genreSuggester = genreSuggester;
        this.watchlistGroupService = watchlistGroupService;
        this.watchlistEntryRepository = watchlistEntryRepository;
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


        List<Movie> movies = movieRepository.findAllMoviesByUserSortedByTitle(user);


        System.out.println("Fetched movies count: " + movies.size());

        movies.forEach(movie -> {
            List<String> groupNames = movie.getWatchlistEntries().stream()
                    .map(entry -> entry.getWatchlistGroup().getName())
                    .collect(Collectors.toList());
            movie.setWatchlistGroupNames(groupNames);

            System.out.println("Movie: " + movie.getTitle() + " | Watchlist Groups: " + groupNames);
        });

        return movies;
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

    @Transactional
    public Movie addMovie(MovieDTO movieDTO, Long userId) {
        if (movieDTO.getWatchlistGroupNames() == null || movieDTO.getWatchlistGroupNames().isEmpty()) {
            throw new IllegalArgumentException("At least one watchlist group name must be provided.");
        }

        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setStatus(movieDTO.getStatus());
        movie.setWatchlistOrder(movieDTO.getWatchlistOrder());

        Genre genre = genreService.getGenreByName(movieDTO.getGenreName())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + movieDTO.getGenreName()));
        movie.setGenre(genre);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: ID " + userId));
        movie.setUser(user);

        Movie savedMovie = movieRepository.save(movie);

        List<WatchlistGroup> watchlistGroups = movieDTO.getWatchlistGroupNames().stream()
                .map(watchlistGroupService::createOrGetWatchlistGroup)
                .collect(Collectors.toList());

        for (WatchlistGroup group : watchlistGroups) {
            WatchlistEntry entry = new WatchlistEntry();
            entry.setMovie(savedMovie);
            entry.setWatchlistGroup(group);
            watchlistEntryRepository.save(entry);
        }

        return savedMovie;
    }



    public Optional<Movie> getMovieById(Long movieId) {
        return movieRepository.findById(movieId);
    }

    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }

    @Transactional
    public void deleteGroupOnly(Long groupId) {
        watchlistGroupService.deleteWatchlistGroup(groupId);
    }

    @Transactional
    public void deleteGroupAndMovies(Long groupId) {
        List<Long> movieIds = watchlistEntryRepository.findByWatchlistGroupId(groupId).stream()
                .map(entry -> entry.getMovie().getMovieId())
                .collect(Collectors.toList());

        watchlistGroupService.deleteWatchlistGroup(groupId);

        for (Long movieId : movieIds) {
            if (watchlistEntryRepository.findByMovieMovieId(movieId).isEmpty()) {
                movieRepository.deleteById(movieId);
            }
        }
    }

    @Transactional
    public Movie editMovie(Long movieId, MovieDTO updatedMovieDTO) {
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        if (updatedMovieDTO.getTitle() != null) {
            existingMovie.setTitle(updatedMovieDTO.getTitle());
        }
        if (updatedMovieDTO.getDescription() != null) {
            existingMovie.setDescription(updatedMovieDTO.getDescription());
        }
        if (updatedMovieDTO.getStatus() != null) {
            existingMovie.setStatus(updatedMovieDTO.getStatus());
        }
        if (updatedMovieDTO.getWatchlistOrder() != null) {
            existingMovie.setWatchlistOrder(updatedMovieDTO.getWatchlistOrder());
        }

        if (updatedMovieDTO.getGenreName() != null) {
            Genre genre = genreService.getGenreByName(updatedMovieDTO.getGenreName())
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + updatedMovieDTO.getGenreName()));
            existingMovie.setGenre(genre);
        }

        if (updatedMovieDTO.getWatchlistGroupNames() == null || updatedMovieDTO.getWatchlistGroupNames().isEmpty()) {
            throw new IllegalArgumentException("At least one watchlist group must be provided.");
        }

        List<WatchlistEntry> existingEntries = watchlistEntryRepository.findByMovieMovieId(movieId);

        List<String> existingGroupNames = existingEntries.stream()
                .map(entry -> entry.getWatchlistGroup().getName())
                .collect(Collectors.toList());

        List<String> newGroupNames = updatedMovieDTO.getWatchlistGroupNames();

        List<WatchlistEntry> entriesToRemove = existingEntries.stream()
                .filter(entry -> !newGroupNames.contains(entry.getWatchlistGroup().getName()))
                .collect(Collectors.toList());

        List<String> groupsToAdd = newGroupNames.stream()
                .filter(name -> !existingGroupNames.contains(name))
                .collect(Collectors.toList());

        watchlistEntryRepository.deleteAll(entriesToRemove);

        for (String groupName : groupsToAdd) {
            WatchlistGroup group = watchlistGroupService.createOrGetWatchlistGroup(groupName);
            WatchlistEntry entry = new WatchlistEntry();
            entry.setMovie(existingMovie);
            entry.setWatchlistGroup(group);
            watchlistEntryRepository.save(entry);
        }

        return movieRepository.save(existingMovie);
    }




    public Movie markAsWatched(Long userId, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        if ("Watched".equals(movie.getStatus())) {
            throw new IllegalStateException("This movie has already been marked as watched");
        }

        movie.setStatus("Watched");
        movieRepository.save(movie);

        if (movie.getUser().isEmailEnabled()) {
            String genre = movie.getGenre().getName();
            String bodyContent;
            try {
                bodyContent = genreSuggester.suggestMovies(genre);
            } catch (Exception e) {
                System.err.println("OpenAI API error: " + e.getMessage());
                bodyContent = "We recommend exploring more movies on this link: https://mubi.com/en/films?sort=popularity_quality_score";
            }

            String to = movie.getUser().getEmail();
            String subject = "Thank you for watching!";
            String body = "Dear user, thank you for watching \"" + movie.getTitle() + "\".\n\n"
                    + "Movie suggestions from the same genre:\n" + bodyContent;

            emailService.sendEmail(to, subject, body);
        }

        return movie;
    }



    public List<Movie> getMoviesByWatchlistGroupAndUser(Long groupId, Long userId) {
        List<Movie> movies = watchlistEntryRepository.findByWatchlistGroupId(groupId).stream()
                .map(WatchlistEntry::getMovie)
                .filter(movie -> movie.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());

        for (Movie movie : movies) {
            List<String> groupNames = movie.getWatchlistEntries().stream()
                    .map(entry -> entry.getWatchlistGroup().getName())
                    .collect(Collectors.toList());
            movie.setWatchlistGroupNames(groupNames);
        }

        return movies;
    }

    public List<Movie> filterMovies(Long userId, String genre, String status, String watchlistOrder, String sort, Long categoryId) {
        List<Movie> movies;

        if ("asc".equalsIgnoreCase(sort)) {
            movies = movieRepository.findAllByUserIdOrderByWatchlistOrderAsc(userId);
        } else if ("desc".equalsIgnoreCase(sort)) {
            movies = movieRepository.findAllByUserIdOrderByWatchlistOrderDesc(userId);
        } else {
            movies = movieRepository.findByUserUserId(userId);
        }

        for (Movie movie : movies) {
            List<String> groupNames = movie.getWatchlistEntries().stream()
                    .map(entry -> entry.getWatchlistGroup().getName())
                    .collect(Collectors.toList());
            movie.setWatchlistGroupNames(groupNames);
        }

        if (genre != null && !genre.isEmpty()) {
            movies = movies.stream()
                    .filter(movie -> movie.getGenre().getName().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }

        if (status != null && !status.isEmpty()) {
            movies = movies.stream()
                    .filter(movie -> movie.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        if (watchlistOrder != null && !watchlistOrder.isEmpty()) {
            movies = movies.stream()
                    .filter(movie -> movie.getWatchlistOrder().equalsIgnoreCase(watchlistOrder))
                    .collect(Collectors.toList());
        }

        if (categoryId != null) {
            movies = movies.stream()
                    .filter(movie -> movie.getWatchlistEntries().stream()
                            .anyMatch(entry -> entry.getWatchlistGroup().getId().equals(categoryId)))
                    .collect(Collectors.toList());
        }

        return movies;
    }






}
