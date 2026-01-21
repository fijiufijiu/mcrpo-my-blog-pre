package com.myblog.dao;

import com.myblog.config.DatabaseConfig;
import com.myblog.dao.impl.PostDaoImpl;
import com.myblog.dao.impl.TagDaoImpl;
import com.myblog.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfig.class, PostDaoImpl.class, TagDaoImpl.class})
@Transactional
class PostDaoIntegrationTest {

    @Autowired
    private PostDao postDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM post_images");
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");
    }

    @Test
    void testCreateAndFindPost() {
        // Given
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Test content");
        post.setTags(Arrays.asList("tag1", "tag2"));

        // When
        Post createdPost = postDao.create(post);

        // Then
        assertNotNull(createdPost.getId());
        assertEquals("Test Post", createdPost.getTitle());
        assertEquals(0, createdPost.getLikesCount());
        
        Optional<Post> foundPost = postDao.findById(createdPost.getId());
        assertTrue(foundPost.isPresent());
        assertEquals(createdPost.getId(), foundPost.get().getId());
        assertEquals(2, foundPost.get().getTags().size());
    }

    @Test
    void testFindAllPosts() {
        // Given
        Post post1 = new Post();
        post1.setTitle("First Post");
        post1.setText("First content");
        post1.setTags(Arrays.asList("java"));
        postDao.create(post1);

        Post post2 = new Post();
        post2.setTitle("Second Post");
        post2.setText("Second content");
        post2.setTags(Arrays.asList("spring"));
        postDao.create(post2);

        // When
        List<Post> posts = postDao.findAll("", 1, 10);

        // Then
        assertEquals(2, posts.size());
    }

    @Test
    void testSearchPostsByTitle() {
        // Given
        Post post1 = new Post();
        post1.setTitle("Java Tutorial");
        post1.setText("Content");
        post1.setTags(Arrays.asList());
        postDao.create(post1);

        Post post2 = new Post();
        post2.setTitle("Spring Framework");
        post2.setText("Content");
        post2.setTags(Arrays.asList());
        postDao.create(post2);

        // When
        List<Post> posts = postDao.findAll("Java", 1, 10);

        // Then
        assertEquals(1, posts.size());
        assertEquals("Java Tutorial", posts.get(0).getTitle());
    }

    @Test
    void testSearchPostsByTag() {
        // Given
        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setText("Content");
        post1.setTags(Arrays.asList("java", "spring"));
        postDao.create(post1);

        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setText("Content");
        post2.setTags(Arrays.asList("python"));
        postDao.create(post2);

        // When
        List<Post> posts = postDao.findAll("#java", 1, 10);

        // Then
        assertEquals(1, posts.size());
        assertEquals("Post 1", posts.get(0).getTitle());
    }

    @Test
    void testUpdatePost() {
        // Given
        Post post = new Post();
        post.setTitle("Original Title");
        post.setText("Original content");
        post.setTags(Arrays.asList("tag1"));
        Post createdPost = postDao.create(post);

        // When
        createdPost.setTitle("Updated Title");
        createdPost.setText("Updated content");
        createdPost.setTags(Arrays.asList("tag2", "tag3"));
        Post updatedPost = postDao.update(createdPost);

        // Then
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Updated content", updatedPost.getText());
        assertEquals(2, updatedPost.getTags().size());
    }

    @Test
    void testDeletePost() {
        // Given
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Test content");
        post.setTags(Arrays.asList());
        Post createdPost = postDao.create(post);

        // When
        postDao.delete(createdPost.getId());

        // Then
        Optional<Post> foundPost = postDao.findById(createdPost.getId());
        assertFalse(foundPost.isPresent());
    }

    @Test
    void testIncrementLikes() {
        // Given
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Test content");
        post.setTags(Arrays.asList());
        Post createdPost = postDao.create(post);

        // When
        postDao.incrementLikes(createdPost.getId());
        postDao.incrementLikes(createdPost.getId());

        // Then
        Optional<Post> updatedPost = postDao.findById(createdPost.getId());
        assertTrue(updatedPost.isPresent());
        assertEquals(2, updatedPost.get().getLikesCount());
    }

    @Test
    void testGetTotalCount() {
        // Given
        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setText("Content");
        post1.setTags(Arrays.asList());
        postDao.create(post1);
        
        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setText("Content");
        post2.setTags(Arrays.asList());
        postDao.create(post2);
        
        Post post3 = new Post();
        post3.setTitle("Post 3");
        post3.setText("Content");
        post3.setTags(Arrays.asList());
        postDao.create(post3);

        // When
        int count = postDao.getTotalCount("");

        // Then
        assertEquals(3, count);
    }
}

