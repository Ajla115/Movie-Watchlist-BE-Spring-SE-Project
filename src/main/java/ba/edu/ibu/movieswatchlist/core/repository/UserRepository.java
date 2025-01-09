package ba.edu.ibu.movieswatchlist.core.repository;
import ba.edu.ibu.movieswatchlist.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u.emailEnabled FROM users u WHERE u.userId = :userId")
    Boolean retrieveEmailStatusByUserId(@Param("userId") Long userId);
}
