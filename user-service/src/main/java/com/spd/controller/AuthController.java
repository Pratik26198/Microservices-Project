package com.spd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.spd.model.User;
import com.spd.repository.UserRepository;
import com.spd.request.LoginRequest;
import com.spd.response.AuthResponse;
import com.spd.service.CustomUserServiceImplementation;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserServiceImplementation customUserDetails;

    @Autowired
    private com.spd.config.JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserhandler(@RequestBody User user) throws Exception {
    	 System.out.println("Received fullName: " + user.getFullName()); 
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String role = user.getRole();

        User isEmailExists = userRepository.findByEmail(email);
        if (isEmailExists != null) {
            throw new Exception("User with this email already exists");
        }

        // Create and save new user
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFullName(fullName);
        createdUser.setRole(role);
        User savedUser = userRepository.save(createdUser);

        // Authenticate and generate JWT
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("User created successfully");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin (@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // Authenticate user
        Authentication authentication = authenticate (username, password);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtProvider.generateToken(authentication);
                AuthResponse authResponse = new AuthResponse();
        
                authResponse.setMessage("User logged in successfully");
                authResponse.setJwt(token);
                authResponse.setStatus(true);

                return new ResponseEntity<>(authResponse, HttpStatus.OK);
            }
        
            private Authentication authenticate(String username, String password) {
                UserDetails userDetails = customUserDetails.loadUserByUsername(username);
                System.out.println("Signed in UserDetails: " + userDetails);
                if (userDetails == null) {
                    System.out.println("sign in userDetails - null"+ username);
                    throw new BadCredentialsException("Invalid username or password");
                }
                if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                    System.out.println("sign in password not match"+ userDetails);
                    throw new BadCredentialsException("Invalid username or password");
                }
                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }
}
