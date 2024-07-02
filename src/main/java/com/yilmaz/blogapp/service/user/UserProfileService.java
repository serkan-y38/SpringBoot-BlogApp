package com.yilmaz.blogapp.service.user;


import com.yilmaz.blogapp.dto.auth.LoginRequestDTO;
import com.yilmaz.blogapp.dto.user.UpdateEmailRequestDTO;
import com.yilmaz.blogapp.dto.user.UpdatePasswordRequestDTO;
import com.yilmaz.blogapp.dto.user.UpdateUserProfileDetailsRequestDTO;
import com.yilmaz.blogapp.dto.user.UserResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface UserProfileService {

    boolean updateProfileDetails(String token, UpdateUserProfileDetailsRequestDTO updateUserProfileDetailsRequestDTO);

    boolean updatePassword(String token, UpdatePasswordRequestDTO updatePasswordRequestDTO);

    boolean updateEmail(HttpServletResponse response, String token, UpdateEmailRequestDTO updateEmailRequestDTO);

    UserResponseDTO getUserProfileDetails(Integer id, String token);

    boolean deleteUserAccount(String token, LoginRequestDTO loginRequestDTO);

}
