package com.myblog.dto;

public class UpdateCommentRequest {
    private Long id;
    private String text;
    private Long postId;

    public UpdateCommentRequest() {}

    public UpdateCommentRequest(Long id, String text, Long postId) {
        this.id = id;
        this.text = text;
        this.postId = postId;
    }

    // Getters
    public Long getId() { return id; }
    public String getText() { return text; }
    public Long getPostId() { return postId; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setText(String text) { this.text = text; }
    public void setPostId(Long postId) { this.postId = postId; }
}
