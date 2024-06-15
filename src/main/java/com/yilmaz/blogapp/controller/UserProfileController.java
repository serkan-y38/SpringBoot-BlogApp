package com.yilmaz.blogapp.controller;

import com.yilmaz.blogapp.dto.user.UpdateUserProfileDetailsRequestDTO;
import com.yilmaz.blogapp.service.user.UserProfileService;
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

}
