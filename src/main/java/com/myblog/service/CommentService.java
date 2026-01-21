package com.myblog.service;

import com.myblog.dto.CreateCommentRequest;
import com.myblog.dto.UpdateCommentRequest;
import com.myblog.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> getCommentsByPostId(Long postId);
    Optional<Comment> getCommentById(Long commentId);
    Comment createComment(CreateCommentRequest request);
    Comment updateComment(Long commentId, UpdateCommentRequest request);
    void deleteComment(Long commentId);
}

