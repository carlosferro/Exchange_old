package com.ferro.exchange.web.controler;

import com.ferro.exchange.domain.User;
import com.ferro.exchange.repository.UserRepository;
import com.ferro.exchange.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping(value="/auth")
public class PublicUserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    // TODO: Deal with multiple signin from same user.
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(
                    username,
                    this.users.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException(
                                    "Username " + username + "not found")).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    // TODO: Deal with duplicated users
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthenticationRequest data) {

        User user = User.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER"))
                .build();

        users.save(user);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), data.getPassword()));
        // TODO: Assume the will always find user
        String token = jwtTokenProvider.createToken(user.getUsername(),
                this.users.findByUsername(user.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Username " + user.getUsername() + "not found")).getRoles());

        Map<Object, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("token", token);
        return ok(model);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationRequest implements Serializable {

        private static final long serialVersionUID = -6986746375915710855L;
        private String username;
        private String password;
    }
}
