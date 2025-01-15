package ba.edu.ibu.movieswatchlist.core.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "watchlist_groups")
public class WatchlistGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "watchlistGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<WatchlistEntry> watchlistEntries;

    public WatchlistGroup(Long id) {
        this.id = id;
    }

    public WatchlistGroup() {
    }

    public WatchlistGroup(Long id, String name, List<WatchlistEntry> watchlistEntries) {
        this.id = id;
        this.name = name;
        this.watchlistEntries = watchlistEntries;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WatchlistEntry> getWatchlistEntries() {
        return watchlistEntries;
    }

    public void setWatchlistEntries(List<WatchlistEntry> watchlistEntries) {
        this.watchlistEntries = watchlistEntries;
    }
}
