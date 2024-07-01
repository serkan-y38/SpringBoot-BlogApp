package com.yilmaz.blogapp.service.post;

import com.yilmaz.blogapp.dto.post.PostRequestResponseDTO;

import java.util.List;

public interface PostService {

    boolean createPost(String token, PostRequestResponseDTO postRequestResponseDTO);

    boolean updatePost(String token, PostRequestResponseDTO postRequestResponseDTO);

    boolean deletePost(String token, Integer userId, Integer postId);

    PostRequestResponseDTO getPostById(String token, Integer postVisitorUserId, Integer postOwnerUserId, Integer postId);

    List<PostRequestResponseDTO> getUserPosts(String token, Integer profileVisitorUserId, Integer profileOwnerUserId, Integer page, Integer size, String orderBy, String direction);

    List<PostRequestResponseDTO> getALlPosts(String token, Integer userId, Integer page, Integer size, String orderBy, String direction);

    List<PostRequestResponseDTO> searchUserPosts(String token, Integer profileVisitorUserId, String query, Integer profileOwnerUserId, Integer page, Integer size, String orderBy, String direction);

    List<PostRequestResponseDTO> searchPosts(String token, Integer userId, String query, Integer page, Integer size, String orderBy, String direction);

}
