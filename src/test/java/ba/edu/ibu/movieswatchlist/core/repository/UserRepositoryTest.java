package ba.edu.ibu.movieswatchlist.core.repository;

import static org.junit.jupiter.api.Assertions.*;
import ba.edu.ibu.movieswatchlist.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.mockito.Mockito.when;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail_UserExists() {

        String email = "korman.ajla115@gmail.com";
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Optional<User> foundUser = userRepository.findByEmail(email);

        assertTrue(foundUser.isPresent(), "User should be found");
        assertEquals(email, foundUser.get().getEmail(), "User email should match");
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {

        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findByEmail(email);

        assertFalse(foundUser.isPresent(), "User should not be found");
    }

    @Test
    void testExistsByEmail_True() {
        String email = "korman.ajla115@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean exists = userRepository.existsByEmail(email);

        assertTrue(exists, "User should exist");
    }

    @Test
    void testExistsByEmail_False() {
        String email = "nonexistent@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        boolean exists = userRepository.existsByEmail(email);

        assertFalse(exists, "User should not exist");
    }
}
