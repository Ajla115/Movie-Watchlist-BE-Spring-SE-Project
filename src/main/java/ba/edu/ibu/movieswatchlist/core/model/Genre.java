package ba.edu.ibu.movieswatchlist.core.model;

import jakarta.persistence.*;

@Entity(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;

    private String name;

    public Genre() {
    }

    public Genre(Long genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }
    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


