package com.myblog.service;

import com.myblog.dto.CreatePostRequest;
import com.myblog.dto.PostListResponse;
import com.myblog.dto.UpdatePostRequest;
import com.myblog.model.Post;

import java.util.Optional;

public interface PostService {
    PostListResponse getPosts(String search, int pageNumber, int pageSize);
    Optional<Post> getPostById(Long id);
    Post createPost(CreatePostRequest request);
    Post updatePost(Long id, UpdatePostRequest request);
    void deletePost(Long id);
    int incrementLikes(Long id);
    int decrementLikes(Long id);
    void saveImage(Long postId, byte[] imageData, String contentType);
    Optional<byte[]> getImage(Long postId);
    Optional<String> getImageContentType(Long postId);
}

