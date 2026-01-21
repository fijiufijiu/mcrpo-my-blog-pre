package com.myblog.dto;

import java.util.List;

public class UpdatePostRequest {
    private Long id;
    private String title;
    private String text;
    private List<String> tags;

    public UpdatePostRequest() {}

    public UpdatePostRequest(Long id, String title, String text, List<String> tags) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public List<String> getTags() { return tags; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
