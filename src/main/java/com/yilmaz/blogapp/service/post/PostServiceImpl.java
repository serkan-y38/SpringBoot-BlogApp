package com.yilmaz.blogapp.service.post;

import com.yilmaz.blogapp.dto.post.PostRequestResponseDTO;
import com.yilmaz.blogapp.entity.Post;
import com.yilmaz.blogapp.entity.User;
import com.yilmaz.blogapp.repository.PostRepository;
import com.yilmaz.blogapp.repository.UserRepository;
import com.yilmaz.blogapp.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtService jwtService;

    @Override
    public boolean createPost(String token, PostRequestResponseDTO dto) {

        Optional<User> optionalUser = userRepository.findById(dto.getUser_id());

        if (optionalUser.isPresent()) {

            if (isRequestedAndAuthenticatedUserSame(token, optionalUser.get().getEmail()))
                return false;

            postRepository.save(
                    Post.builder()
                            .title(dto.getTitle())
                            .body(dto.getBody())
                            .date(dto.getDate())
                            .likeNum(0)
                            .user(optionalUser.get())
                            .build()
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePost(String token, Integer userId, Integer postId) {

        Optional<Post> optionalPost = postRepository.findById(postId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalPost.isPresent() && optionalUser.isPresent()) {

            if (isRequestedAndAuthenticatedUserSame(token, optionalUser.get().getEmail()))
                return false;

            postRepository.delete(optionalPost.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePost(String token, PostRequestResponseDTO dto) {

        Optional<Post> optionalPost = postRepository.findById(dto.getId());
        Optional<User> optionalUser = userRepository.findById(dto.getUser_id());

        if (optionalPost.isPresent() && optionalUser.isPresent()) {

            if (isRequestedAndAuthenticatedUserSame(token, optionalUser.get().getEmail()))
                return false;

            Post post = optionalPost.get();
            post.setTitle(dto.getTitle());
            post.setBody(dto.getBody());
            post.setDate(dto.getDate());

            postRepository.save(post);
            return true;
        }
        return false;
    }

    @Override
    public PostRequestResponseDTO getPostById(
            String token,
            Integer postVisitorUserId,
            Integer postOwnerUserId,
            Integer postId
    ) {
        Optional<User> optionalPostOwner = userRepository.findById(postOwnerUserId);
        Optional<User> optionalPostVisitor = userRepository.findById(postVisitorUserId);

        if (optionalPostOwner.isPresent() && optionalPostVisitor.isPresent()) {

            if (isRequestedAndAuthenticatedUserSame(token, optionalPostVisitor.get().getEmail()))
                return null;

            Optional<Post> optionalPost = postRepository.findByIdAndUser(postId, optionalPostOwner.get());
            return optionalPost.map(Post::getPostRequestResponseDTO).orElse(null);
        }
        return null;
    }

    @Override
    public List<PostRequestResponseDTO> getUserPosts(
            String token,
            Integer profileVisitorUserId,
            Integer profileOwnerUserId,
            Integer page,
            Integer size,
            String orderBy,
            String direction
    ) {
        Optional<User> optionalPostOwner = userRepository.findById(profileOwnerUserId);
        Optional<User> optionalPostVisitor = userRepository.findById(profileVisitorUserId);

        if (optionalPostOwner.isPresent() && optionalPostVisitor.isPresent()) {

            if (isRequestedAndAuthenticatedUserSame(token, optionalPostVisitor.get().getEmail()))
                return null;

            PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
            Page<Post> postPage = postRepository.findAllByUser(optionalPostOwner.get(), pageRequest);
            return postPage.stream().map(Post::getPostRequestResponseDTO).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<PostRequestResponseDTO> getALlPosts(
            String token,
            Integer userId,
            Integer page,
            Integer size,
            String orderBy,
            String direction
    ) {
        Optional<User> optionalPostVisitor = userRepository.findById(userId);

        if (optionalPostVisitor.isPresent()) {

            if (isRequestedAndAuthenticatedUserSame(token, optionalPostVisitor.get().getEmail()))
                return null;

            PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
            Page<Post> postPage = postRepository.findAll(pageRequest);
            return postPage.stream().map(Post::getPostRequestResponseDTO).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<PostRequestResponseDTO> searchUserPosts(
            String token,
            Integer profileVisitorUserId,
            String query,
            Integer profileOwnerUserId,
            Integer page,
            Integer size,
            String orderBy,
            String direction
    ) {
        Optional<User> optionalPostOwner = userRepository.findById(profileOwnerUserId);
        Optional<User> optionalPostVisitor = userRepository.findById(profileVisitorUserId);

        if (optionalPostOwner.isPresent() && optionalPostVisitor.isPresent()) {

            if (isRequestedAndAuthenticatedUserSame(token, optionalPostVisitor.get().getEmail()))
                return null;

            PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
            Page<Post> postPage = postRepository.findAllByTitleContainingAndUser(query, optionalPostOwner.get(), pageRequest);
            return postPage.stream().map(Post::getPostRequestResponseDTO).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<PostRequestResponseDTO> searchPosts(
            String token,
            Integer userId,
            String query,
            Integer page,
            Integer size,
            String orderBy,
            String direction
    ) {
        Optional<User> optionalPostVisitor = userRepository.findById(userId);

        if (optionalPostVisitor.isPresent()) {

            if (isRequestedAndAuthenticatedUserSame(token, optionalPostVisitor.get().getEmail()))
                return null;

            PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
            Page<Post> postPage = postRepository.findAllByTitleContaining(query, pageRequest);
            return postPage.stream().map(Post::getPostRequestResponseDTO).collect(Collectors.toList());
        }
        return null;
    }

    private boolean isRequestedAndAuthenticatedUserSame(String token, String email) {
        return !jwtService.extractUsername(token).equals(email); // when authenticated user and requested user same
    }

}
