package com.yilmaz.blogapp.repository;

import com.yilmaz.blogapp.entity.Post;
import com.yilmaz.blogapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findByIdAndUser(Integer id, User user);

    Page<Post> findAllByUser(User user, Pageable pageable);

    Page<Post> findAllByTitleContainingAndUser(String title, User user, Pageable pageable);

    Page<Post> findAllByTitleContaining(String title, Pageable pageable);

}
