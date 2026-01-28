package com.myblog.controller;

import com.myblog.dto.CreatePostRequest;
import com.myblog.dto.UpdatePostRequest;
import com.myblog.model.Post;
import com.myblog.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/posts?search={}&page={}&size={}", search, page, size);
        List<Post> posts = postService.getAllPosts(search, page, size);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        log.debug("GET /api/posts/{}", id);
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody CreatePostRequest request) {
        log.debug("POST /api/posts - title: {}", request.getTitle());
        Post createdPost = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest request) {
        log.debug("PUT /api/posts/{} - title: {}", id, request.getTitle());
        try {
            Post updatedPost = postService.updatePost(id, request);
            return ResponseEntity.ok(updatedPost);
        } catch (IllegalArgumentException e) {
            log.warn("Post not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating post {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.debug("DELETE /api/posts/{}", id);
        try {
            postService.deletePost(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warn("Post not found for deletion: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting post {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> addLike(@PathVariable Long id) {
        log.debug("POST /api/posts/{}/like", id);
        try {
            postService.incrementLikes(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warn("Cannot add like: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error adding like to post {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> removeLike(@PathVariable Long postId) {
        log.debug("DELETE /api/posts/{}/like", postId);
        try {
            postService.decrementLikes(postId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warn("Cannot remove like: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error removing like from post {}: {}", postId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getPostImage(@PathVariable Long id) {
        log.debug("GET /api/posts/{}/image", id);
        return postService.getPostImage(id)
                .map(imageData -> {
                    String contentType = postService.getImageContentType(id).orElse("image/jpeg");
                    return ResponseEntity.ok()
                            .header("Content-Type", contentType)
                            .body(imageData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Void> uploadPostImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image) {
        log.debug("POST /api/posts/{}/image", id);
        try {
            if (image.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            postService.saveImage(id, image.getBytes(), image.getContentType());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error uploading image for post {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getTotalPosts(@RequestParam(required = false) String search) {
        log.debug("GET /api/posts/count?search={}", search);
        int count = postService.getTotalCount(search);
        return ResponseEntity.ok(count);
    }
}