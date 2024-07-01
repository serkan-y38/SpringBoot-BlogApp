package com.yilmaz.blogapp.entity;

import com.yilmaz.blogapp.dto.post.PostRequestResponseDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "post_table")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String body;
    private Date date;
    private long likeNum;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public PostRequestResponseDTO getPostRequestResponseDTO() {
        return PostRequestResponseDTO.builder()
                .id(id)
                .title(title)
                .body(body)
                .date(date)
                .likeNum(likeNum)
                .user_id(user.getId())
                .build();
    }

}
