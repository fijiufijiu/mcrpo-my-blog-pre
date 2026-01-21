package com.myblog.dto;

import com.myblog.model.Post;
import java.util.List;

public class PostListResponse {
    private List<Post> posts;
    private boolean hasPrev;
    private boolean hasNext;
    private int lastPage;

    public PostListResponse() {}

    public PostListResponse(List<Post> posts, boolean hasPrev, boolean hasNext, int lastPage) {
        this.posts = posts;
        this.hasPrev = hasPrev;
        this.hasNext = hasNext;
        this.lastPage = lastPage;
    }

    // Getters
    public List<Post> getPosts() { return posts; }
    public boolean isHasPrev() { return hasPrev; }
    public boolean isHasNext() { return hasNext; }
    public int getLastPage() { return lastPage; }

    // Setters
    public void setPosts(List<Post> posts) { this.posts = posts; }
    public void setHasPrev(boolean hasPrev) { this.hasPrev = hasPrev; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
    public void setLastPage(int lastPage) { this.lastPage = lastPage; }
}
