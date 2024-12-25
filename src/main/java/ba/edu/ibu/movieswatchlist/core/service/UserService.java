package ba.edu.ibu.movieswatchlist.core.service;

import ba.edu.ibu.movieswatchlist.core.model.User;
import ba.edu.ibu.movieswatchlist.core.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User addUser(String email) {
        User user = new User(email);
        user.setEmailEnabled(true); // Default value
        return userRepository.save(user);
    }

    public void changeNotificationStatus(Long userId, boolean emailEnabled) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            user.setEmailEnabled(emailEnabled);
            userRepository.save(user);
        });
    }
}