package com.myblog.dao;

import com.myblog.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    Post create(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAll(String search, int pageNumber, int pageSize);
    Post update(Post post);
    void delete(Long id);
    void incrementLikes(Long id);
    void decrementLikes(Long id);
    int getTotalCount(String search);
    void saveImage(Long postId, byte[] imageData, String contentType);
    Optional<byte[]> getImage(Long postId);
    Optional<String> getImageContentType(Long postId);
}

