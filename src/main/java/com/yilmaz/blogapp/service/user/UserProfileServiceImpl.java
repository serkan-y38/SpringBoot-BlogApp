package com.yilmaz.blogapp.service.user;

import com.yilmaz.blogapp.dto.user.UpdateUserProfileDetailsRequestDTO;
import com.yilmaz.blogapp.entity.User;
import com.yilmaz.blogapp.repository.UserRepository;
import com.yilmaz.blogapp.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public boolean updateProfileDetails(String token, UpdateUserProfileDetailsRequestDTO updateUserProfileDetailsRequestDTO) {

        String email = updateUserProfileDetailsRequestDTO.getEmail();

        if (!jwtService.extractUsername(token).equals(email))
            return false; // when authenticated user and requested user not same

        Optional<User> optionalUser = userRepository.findByEmail(updateUserProfileDetailsRequestDTO.getEmail());

        if (optionalUser.isPresent() && optionalUser.get().getEmail().equals(email)) {
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

}
