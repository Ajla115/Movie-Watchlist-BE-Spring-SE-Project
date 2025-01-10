package ba.edu.ibu.movieswatchlist.core.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "watchlist_entries")
public class WatchlistEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "watchlist_group_id", nullable = false)
    @JsonBackReference
    private WatchlistGroup watchlistGroup;

    public WatchlistEntry(Long id) {
        this.id = id;
    }

    public WatchlistEntry() {
    }

    public WatchlistEntry(Long id, Movie movie, WatchlistGroup watchlistGroup) {
        this.id = id;
        this.movie = movie;
        this.watchlistGroup = watchlistGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public WatchlistGroup getWatchlistGroup() {
        return watchlistGroup;
    }

    public void setWatchlistGroup(WatchlistGroup watchlistGroup) {
        this.watchlistGroup = watchlistGroup;
    }
}
