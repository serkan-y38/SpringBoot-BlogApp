package com.yilmaz.blogapp.entity;

import com.yilmaz.blogapp.dto.user.UserResponse;
import com.yilmaz.blogapp.entity.enums.Role;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;
    private String password;
    private String firstname;
    private String lastname;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;

    public UserResponse getUserResponse() {
        return UserResponse.builder()
                .id(id)
                .role(role)
                .firstname(firstname)
                .lastname(lastname)
                .img(img)
                .build();
    }

}