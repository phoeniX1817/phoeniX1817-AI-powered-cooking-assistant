package com.smartrecipe.cooking_assistant.Controller;

import com.smartrecipe.cooking_assistant.Models.User;
import com.smartrecipe.cooking_assistant.Repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Set<String> activeUsers = Collections.synchronizedSet(new HashSet<>());

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("❌ Username already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        userRepository.save(user);
        return ResponseEntity.ok("✅ User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpSession session) {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        if (foundUser.isPresent() && passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
            if (session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("❌ User already logged in!");
            }
            session.setAttribute("user", user.getUsername()); // ✅ Store session
            activeUsers.add(user.getUsername()); // ✅ Track user login
            return ResponseEntity.ok("✅ Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            activeUsers.remove(session.getAttribute("user")); // ✅ Remove from active users
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return ResponseEntity.ok("✅ Logged out successfully!");
    }

}
