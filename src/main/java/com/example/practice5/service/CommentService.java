package com.example.practice5.service;

import com.example.practice5.model.Comment;
import com.example.practice5.model.Post;
import com.example.practice5.model.User;
import com.example.practice5.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> findAllByUser(User user) {
        return commentRepository.findAllByUser(user);
    }

    public List<Comment> findAllByPost(Post post) {
        return commentRepository.findAllByPost(post);
    }

    @Transactional
    public void create(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
