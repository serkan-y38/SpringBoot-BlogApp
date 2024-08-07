package com.yilmaz.blogapp.controller;

import com.yilmaz.blogapp.dto.auth.LoginRequestDTO;
import com.yilmaz.blogapp.dto.auth.RegisterRequestDTO;
import com.yilmaz.blogapp.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RegisterRequestDTO request) throws IOException {
        return service.register(request) ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PostMapping("/login")
    public void login(
            @RequestBody LoginRequestDTO request,
            HttpServletResponse response
    ) throws IOException, JSONException {
        service.login(request, response);
    }

}