package com.myblog.dao;

import com.myblog.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDao {
    Comment create(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findByPostId(Long postId);
    Comment update(Comment comment);
    void delete(Long id);
    int countByPostId(Long postId);
}

