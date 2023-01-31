package com.contextlab.authservice.controller;

import com.contextlab.authservice.config.JwtGeneratorInterface;
import com.contextlab.authservice.exception.UserNotFoundException;
import com.contextlab.authservice.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserController {
    @Autowired
    private JwtGeneratorInterface jwtGenerator;

    @Value("${app.user}")
    private String testUser;
    @Value("${app.pass}")
    private String testPassword;
    @Value("${app.controller.exception.message1}")
    private String errorMessage1;
    @Value("${app.controller.exception.message2}")
    private String errorMessage2;

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            if(user.getUsername() == null || user.getPassword() == null) {
                throw new UserNotFoundException(errorMessage1);
            }
            if(!testUser.equals(user.getUsername()) ||
                    !testPassword.equals(user.getPassword()) ){
                throw new UserNotFoundException(errorMessage2);
            }
            return new ResponseEntity<>(jwtGenerator.generateToken(user), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
