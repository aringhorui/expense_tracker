package com.aringhorui.Service;

import com.aringhorui.Entities.User;
import com.aringhorui.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        // check if email already exists (primary key)
        if (userRepository.existsById(user.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }

        // check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already in use!");
        }

        // hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Check password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user; // login successful
    }

    public User getUserByEmail(String email) {
        return userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public void deleteUser(String email) {
        if (!userRepository.existsById(email)) {
            throw new RuntimeException("User not found with email: " + email);
        }
        userRepository.deleteById(email);
    }
}
