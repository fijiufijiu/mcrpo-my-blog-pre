package com.myblog.dao;

import com.myblog.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    Tag create(String tagName);
    Optional<Tag> findByName(String name);
    List<Tag> findByPostId(Long postId);
    void linkTagToPost(Long tagId, Long postId);
    void unlinkAllTagsFromPost(Long postId);
}

