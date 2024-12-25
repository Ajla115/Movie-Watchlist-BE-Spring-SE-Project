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

    public Long addOrGetUserId(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            // If user exists, return the user ID
            return existingUser.get().getUserId();
        } else {
            // If user does not exist, create a new user and return its ID
            User newUser = new User(email);
            newUser.setEmailEnabled(true); // Default value
            User savedUser = userRepository.save(newUser);
            return savedUser.getUserId();
        }
    }

    // It is not used anymore
    public User addUser(String email) {
        User user = new User(email);
        user.setEmailEnabled(true); // Default value
        return userRepository.save(user);
    }

    public void toggleNotificationStatus(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            // Toggle the current status
            user.setEmailEnabled(!user.isEmailEnabled());
            userRepository.save(user);
        });
    }
}