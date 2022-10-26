package com.example.practice5.repository;

import com.example.practice5.model.Comment;
import com.example.practice5.model.Post;
import com.example.practice5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUser(User user);
    List<Comment> findAllByPost(Post post);
}
