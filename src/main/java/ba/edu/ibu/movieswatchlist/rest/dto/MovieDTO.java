package ba.edu.ibu.movieswatchlist.rest.dto;

import java.util.List;

public class MovieDTO {
    private String title;
    private String description;
    private String status;
    private String watchlistOrder;
    private String genreName;
    private List<String> watchlistGroupNames;



    public MovieDTO() {}

    public MovieDTO(String title, String description, String status, String watchlistOrder, String genreName, List<String> watchlistGroupNames) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.watchlistOrder = watchlistOrder;
        this.genreName = genreName;
        this.watchlistGroupNames = watchlistGroupNames;
    }

    public List<String> getWatchlistGroupNames() {
        return watchlistGroupNames;
    }

    public void setWatchlistGroupNames(List<String> watchlistGroupNames) {
        this.watchlistGroupNames = watchlistGroupNames;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWatchlistOrder() {
        return watchlistOrder;
    }

    public void setWatchlistOrder(String watchlistOrder) {
        this.watchlistOrder = watchlistOrder;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
