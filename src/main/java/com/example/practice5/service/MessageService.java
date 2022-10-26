package com.example.practice5.service;

import com.example.practice5.model.Message;
import com.example.practice5.model.User;
import com.example.practice5.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getConversation(User user1, User user2) {
        List<Message> messages = messageRepository.findAllBySenderAndReceiver(user1, user2);
        List<Message> messages1 = messageRepository.findAllBySenderAndReceiver(user2, user1);
        messages.addAll(messages1);
        messages.sort(Comparator.comparing(Message::getTime));
        return messages;
    }

    @Transactional
    public void create(Message message) {
        messageRepository.save(message);
    }
}
