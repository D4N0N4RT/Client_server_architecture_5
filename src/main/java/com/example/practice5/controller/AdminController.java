package com.example.practice5.controller;

import com.example.practice5.exception.WrongIdException;
import com.example.practice5.model.Comment;
import com.example.practice5.model.Post;
import com.example.practice5.model.User;
import com.example.practice5.service.CommentService;
import com.example.practice5.service.PostService;
import com.example.practice5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public AdminController(CommentService commentService, UserService userService, PostService postService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestParam @NotBlank String email) {
        User user = (User) userService.loadUserByUsername(email);
        userService.delete(user);
        return new ResponseEntity<>("Пользователь удален", HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "id") long id) throws WrongIdException {
        Comment comment = commentService.findById(id).orElseThrow(() -> new WrongIdException("Неправльный id"));
        commentService.delete(comment);
        return new ResponseEntity<>("Комментарий удален", HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(name = "id") long id) throws WrongIdException {
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("Неправльный id"));
        postService.delete(post);
        return new ResponseEntity<>("Объявление удалено", HttpStatus.OK);
    }
}
