package com.yilmaz.blogapp.service.user;

import com.yilmaz.blogapp.dto.user.UpdateEmailRequestDTO;
import com.yilmaz.blogapp.dto.user.UpdatePasswordRequestDTO;
import com.yilmaz.blogapp.dto.user.UpdateUserProfileDetailsRequestDTO;
import com.yilmaz.blogapp.entity.User;
import com.yilmaz.blogapp.repository.UserRepository;
import com.yilmaz.blogapp.service.auth.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public boolean updateProfileDetails(String token, UpdateUserProfileDetailsRequestDTO updateUserProfileDetailsRequestDTO) {

        if (isRequestedAndAuthenticatedUserSame(token, updateUserProfileDetailsRequestDTO.getEmail())) return false;

        Optional<User> optionalUser = userRepository.findByEmail(updateUserProfileDetailsRequestDTO.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstname(updateUserProfileDetailsRequestDTO.getFirstname());
            user.setLastname(updateUserProfileDetailsRequestDTO.getLastname());

            byte[] profileImg = null;
            if (updateUserProfileDetailsRequestDTO.getImg() != null) {
                try {
                    profileImg = updateUserProfileDetailsRequestDTO.getImg().getBytes();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            user.setImg(profileImg);

            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePassword(String token, UpdatePasswordRequestDTO updatePasswordRequestDTO) {

        if (isRequestedAndAuthenticatedUserSame(token, updatePasswordRequestDTO.getEmail()))
            return false;

        var springUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!passwordEncoder.matches(updatePasswordRequestDTO.getCurrentPassword(), springUser.getPassword()))
            return false;

        Optional<User> optionalUser = userRepository.findByEmail(updatePasswordRequestDTO.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(updatePasswordRequestDTO.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEmail(HttpServletResponse response, String token, UpdateEmailRequestDTO updateEmailRequestDTO) {

        if (isRequestedAndAuthenticatedUserSame(token, updateEmailRequestDTO.getCurrentEmail()))
            return false;

        var springUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!passwordEncoder.matches(updateEmailRequestDTO.getPassword(), springUser.getPassword()))
            return false;

        Optional<User> optionalUser = userRepository.findByEmail(updateEmailRequestDTO.getCurrentEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(updateEmailRequestDTO.getNewEmail());
            userRepository.save(user);
            regenerateToken(response, user.getEmail());
            return true;
        }
        return false;
    }

    private void regenerateToken(HttpServletResponse response, String email) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String jwt = jwtService.generateToken(new HashMap<>(), userDetails);
        response.addHeader("Authorization", "Bearer " + jwt);
    }

    private boolean isRequestedAndAuthenticatedUserSame(String token, String email) {
        return !jwtService.extractUsername(token).equals(email); // when authenticated user and requested user same
    }

}
