package com.yilmaz.blogapp.controller;

import com.yilmaz.blogapp.dto.post.PostRequestResponseDTO;
import com.yilmaz.blogapp.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create-post")
    public ResponseEntity<?> createPost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
            @RequestBody PostRequestResponseDTO postRequestResponseDTO
    ) {
        return postService.createPost(header.substring(7), postRequestResponseDTO)
                ? new ResponseEntity<>("Post created successfully", HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PutMapping("/update-post")
    public ResponseEntity<?> updatePost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
            @RequestBody PostRequestResponseDTO postRequestResponseDTO
    ) {
        return postService.updatePost(header.substring(7), postRequestResponseDTO)
                ? new ResponseEntity<>("Post updated successfully", HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @DeleteMapping("/delete-post/{userId}/{postId}")
    public ResponseEntity<?> deletePost(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
            @PathVariable Integer userId,
            @PathVariable Integer postId
    ) {
        return postService.deletePost(header.substring(7), userId, postId)
                ? new ResponseEntity<>("Post deleted successfully", HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @GetMapping("/get-post/{postVisitorUserId}/{postOwnerUserId}/{postId}")
    public ResponseEntity<?> getPostById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
            @PathVariable Integer postVisitorUserId,
            @PathVariable Integer postOwnerUserId,
            @PathVariable Integer postId
    ) {
        PostRequestResponseDTO post = postService.getPostById(header.substring(7), postVisitorUserId, postOwnerUserId, postId);
        return (post != null) ? ResponseEntity.ok(post) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/get-user-posts/{profileVisitorUserId}/{profileOwnerUserId}")
    public ResponseEntity<?> getUserPosts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
            @PathVariable Integer profileVisitorUserId,
            @PathVariable Integer profileOwnerUserId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        List<PostRequestResponseDTO> post = postService.getUserPosts(header.substring(7), profileVisitorUserId, profileOwnerUserId, page, size, orderBy, direction);
        return (post != null) ? ResponseEntity.ok(post) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/get-all-posts/{userId}")
    public ResponseEntity<?> getAllPosts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
            @PathVariable Integer userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        List<PostRequestResponseDTO> post = postService.getALlPosts(header.substring(7), userId, page, size, orderBy, direction);
        return (post != null) ? ResponseEntity.ok(post) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/search-posts/{userId}")
    public ResponseEntity<?> searchPosts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
            @PathVariable Integer userId,
            @RequestParam(value = "query") String query,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        List<PostRequestResponseDTO> post = postService.searchPosts(header.substring(7), userId, query, page, size, orderBy, direction);
        return (post != null) ? ResponseEntity.ok(post) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
