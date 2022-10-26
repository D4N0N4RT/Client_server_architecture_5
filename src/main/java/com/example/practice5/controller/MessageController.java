package com.example.practice5.controller;

import com.example.practice5.dto.MessageDTO;
import com.example.practice5.model.Message;
import com.example.practice5.model.User;
import com.example.practice5.security.JwtTokenProvider;
import com.example.practice5.service.MessageService;
import com.example.practice5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MessageController(MessageService messageService, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.messageService = messageService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("")
    public ResponseEntity<?> getConversation(@RequestParam(name="email") @NotBlank String email, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user1 = (User) userService.loadUserByUsername(username);
        User user2 = (User) userService.loadUserByUsername(email);
        List<Message> messageList = messageService.getConversation(user1, user2);
        List<MessageDTO> conversation = messageList.stream()
                .map(Message::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(conversation, HttpStatus.FOUND);
    }

    @PostMapping("")
    public ResponseEntity<?> sendMessage(@RequestParam(name="email") @NotBlank String email,
                                         @RequestBody @Valid @NotBlank(message = "Сообщение не может быть пустым") String content,
                                         HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user1 = (User) userService.loadUserByUsername(username);
        User user2 = (User) userService.loadUserByUsername(email);
        Message message = Message.builder().sender(user1).receiver(user2)
                .content(content).time(LocalDateTime.now()).build();
        messageService.create(message);
        return new ResponseEntity<>("Ваше сообщение отправлено", HttpStatus.CREATED);
    }
}