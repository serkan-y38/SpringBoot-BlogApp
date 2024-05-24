package com.yilmaz.blogapp.service.auth;

import com.yilmaz.blogapp.dto.auth.LoginRequest;
import com.yilmaz.blogapp.dto.auth.RegisterRequest;
import com.yilmaz.blogapp.entity.User;
import com.yilmaz.blogapp.entity.enums.Role;
import com.yilmaz.blogapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public boolean register(RegisterRequest request) throws IOException {
        if (!isMailAddressExist(request.getEmail())) {
            byte[] profileImg = null;
            if (request.getImg() != null)
                profileImg = request.getImg().getBytes();
            repository.save(
                    User.builder()
                            .firstname(request.getFirstname())
                            .lastname(request.getLastname())
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(Role.USER)
                            .img(profileImg)
                            .build()
            );
            return true;
        }
        return false;
    }

    private boolean isMailAddressExist(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public void login(LoginRequest request, HttpServletResponse response) throws IOException, JSONException {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtService.generateToken(new HashMap<>(), userDetails);
        final Optional<User> user = repository.findByEmail(request.getEmail());

        if (user.isPresent()) {
            response.addHeader("Authorization", "Bearer " + jwt);
            response.getWriter().write(
                    new JSONObject().
                            put("id", user.get().getId()).
                            put("role", user.get().getRole()).
                            put("firstname", user.get().getFirstname()).
                            put("lastname", user.get().getLastname()).toString()
            );
        }

    }

}