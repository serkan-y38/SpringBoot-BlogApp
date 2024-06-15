package com.yilmaz.blogapp.service.user;


import com.yilmaz.blogapp.dto.user.UpdateUserProfileDetailsRequestDTO;

public interface UserProfileService {

    boolean updateProfileDetails(String token, UpdateUserProfileDetailsRequestDTO updateUserProfileDetailsRequestDTO);

}
