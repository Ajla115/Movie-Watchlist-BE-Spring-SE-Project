package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.core.model.User;
import ba.edu.ibu.movieswatchlist.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByEmail_UserExists() {
        String email = "korman.ajla115@gmail.com";
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUserByEmail_UserDoesNotExist() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail(email);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testAddOrGetUserId_UserExists() {
        String email = "korman.ajla115@gmail.com";
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Long userId = userService.addOrGetUserId(email);

        assertEquals(1L, userId);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAddOrGetUserId_UserDoesNotExist() {
        String email = "newuser@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setUserId(2L);
            return savedUser;
        });

        Long userId = userService.addOrGetUserId(email);

        assertEquals(2L, userId);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAddUser() {
        String email = "newuser@example.com";
        User mockUser = new User(email);
        mockUser.setUserId(3L);
        mockUser.setEmailEnabled(true);

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.addUser(email);

        assertEquals(email, result.getEmail());
        assertTrue(result.isEmailEnabled());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testToggleNotificationStatus_UserExists() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setEmailEnabled(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        userService.toggleNotificationStatus(userId);

        assertFalse(mockUser.isEmailEnabled());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testToggleNotificationStatus_UserDoesNotExist() {
        Long userId = 11L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.toggleNotificationStatus(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}
