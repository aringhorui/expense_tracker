package com.aringhorui.Controller;

import com.aringhorui.Entities.User;
import com.aringhorui.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // allows frontend requests (e.g. React, Angular)
public class UserController {

    @Autowired
    private UserService userService;

    // 🟢 Register a new user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // 🟡 Get user by email (primary key)
    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    // 🔴 Delete user by email
    @DeleteMapping("/{email}")
    public String deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return "User deleted successfully: " + email;
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody LoginRequest request) {
        return userService.loginUser(request.getEmail(), request.getPassword());
    }

    // DTO for login
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
