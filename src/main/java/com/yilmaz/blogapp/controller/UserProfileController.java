package com.yilmaz.blogapp.controller;

import com.yilmaz.blogapp.dto.auth.LoginRequestDTO;
import com.yilmaz.blogapp.dto.user.UpdateEmailRequestDTO;
import com.yilmaz.blogapp.dto.user.UpdatePasswordRequestDTO;
import com.yilmaz.blogapp.dto.user.UpdateUserProfileDetailsRequestDTO;
import com.yilmaz.blogapp.dto.user.UserResponseDTO;
import com.yilmaz.blogapp.service.user.UserProfileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/update-profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/update-profile-details")
    public ResponseEntity<?> updateProfileDetails(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @ModelAttribute UpdateUserProfileDetailsRequestDTO request
    ) {
        return userProfileService.updateProfileDetails(authHeader.substring(7), request)
                ? new ResponseEntity<>("Profile updated successfully", HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PatchMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody UpdatePasswordRequestDTO request
    ) {
        return userProfileService.updatePassword(authHeader.substring(7), request)
                ? new ResponseEntity<>("Password updated successfully", HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PatchMapping("/update-email")
    public ResponseEntity<?> updateEmail(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody UpdateEmailRequestDTO request,
            HttpServletResponse response
    ) {
        return userProfileService.updateEmail(response, authHeader.substring(7), request)
                ? new ResponseEntity<>("Email updated successfully", HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PatchMapping("/delete-account")
    public ResponseEntity<?> deleteUserAccount(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody LoginRequestDTO request
    ) {
        return userProfileService.deleteUserAccount(authHeader.substring(7), request)
                ? new ResponseEntity<>("Account deleted successfully", HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @GetMapping("/get-user-profile-details/{userId}")
    public ResponseEntity<?> getUserProfileDetails(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable Integer userId
    ) {
        UserResponseDTO dto = userProfileService.getUserProfileDetails(userId, authHeader.substring(7));
        return (dto != null) ? ResponseEntity.ok(dto) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
