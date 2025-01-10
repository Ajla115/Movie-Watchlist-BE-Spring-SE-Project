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
            return existingUser.get().getUserId();
        } else {
            User newUser = new User(email);
            newUser.setEmailEnabled(true);
            User savedUser = userRepository.save(newUser);
            return savedUser.getUserId();
        }
    }

    public User addUser(String email) {
        User user = new User(email);
        user.setEmailEnabled(true);
        return userRepository.save(user);
    }

    public void toggleNotificationStatus(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            user.setEmailEnabled(!user.isEmailEnabled());
            userRepository.save(user);
        });
    }

    public Boolean getUserNotificationStatus(Long userId) {
        return userRepository.retrieveEmailStatusByUserId(userId);
    }
}