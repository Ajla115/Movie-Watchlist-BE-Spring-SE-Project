package ba.edu.ibu.movieswatchlist.core.model;

import jakarta.persistence.*;

@Entity(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    private String title;

    private String description;

    private String watchlistOrder;

    private String status;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false, foreignKey = @ForeignKey(name = "fk_movie_genre"))
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_movie_user"))
    private User user;

    public Movie() {
    }

    public Movie(Long movieId, String title, String description, String watchlistOrder, String status, Genre genre, User user) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.watchlistOrder = watchlistOrder;
        this.status = status;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWatchlistOrder() {
        return watchlistOrder;
    }

    public void setWatchlistOrder(String watchlistOrder) {
        this.watchlistOrder = watchlistOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
