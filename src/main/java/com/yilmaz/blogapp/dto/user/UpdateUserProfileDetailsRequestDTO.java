package com.yilmaz.blogapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfileDetailsRequestDTO {
    private String email;
    private String firstname;
    private String lastname;
    private MultipartFile img;
}