package ba.edu.ibu.movieswatchlist.core.model;

import jakarta.persistence.*;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private boolean emailEnabled;

    public User() {
    }

    public User(Long userId, String email, boolean emailEnabled) {
        this.userId = userId;
        this.email = email;
        this.emailEnabled = emailEnabled;
    }

    public User(String email) {
        this.email = email;
        this.emailEnabled = true;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }
}
