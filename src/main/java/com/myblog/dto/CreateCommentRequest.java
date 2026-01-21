package com.myblog.dto;

public class CreateCommentRequest {
    private String text;
    private Long postId;

    public CreateCommentRequest() {}

    public CreateCommentRequest(String text, Long postId) {
        this.text = text;
        this.postId = postId;
    }

    // Getters
    public String getText() { return text; }
    public Long getPostId() { return postId; }

    // Setters
    public void setText(String text) { this.text = text; }
    public void setPostId(Long postId) { this.postId = postId; }
}
