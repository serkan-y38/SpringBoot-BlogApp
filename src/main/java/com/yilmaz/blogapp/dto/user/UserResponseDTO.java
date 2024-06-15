package com.yilmaz.blogapp.dto.user;

import com.yilmaz.blogapp.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Integer id;
    private Role role;
    private String firstname;
    private String lastname;
    private byte[] img;
}
