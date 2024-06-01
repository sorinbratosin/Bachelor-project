package com.sorinbratosin.licenta.Controller;

import com.sorinbratosin.licenta.POJO.User;
import com.sorinbratosin.licenta.Service.JwtService;
import com.sorinbratosin.licenta.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.registerUser(user) != null) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.badRequest().body("Error registering user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User authenticatedUser = userService.loginUser(user.getEmail(), user.getPassword());
        if (authenticatedUser != null) {
            // Creează un obiect de răspuns care conține tokenul și userId
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User logged in successfully");
            response.put("userId", authenticatedUser.getId());
            response.put("token", jwtService.generateToken(authenticatedUser));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
