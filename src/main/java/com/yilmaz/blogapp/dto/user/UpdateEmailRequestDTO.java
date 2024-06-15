package com.yilmaz.blogapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmailRequestDTO {
    private String currentEmail;
    private String newEmail;
    private String password;
}
