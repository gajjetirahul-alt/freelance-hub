package com.freelancehub.service;

import com.freelancehub.model.Message;
import com.freelancehub.model.User;
import com.freelancehub.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepo;
    private final UserService userService;
    private final ProjectService projectService;

    public MessageService(MessageRepository messageRepo, UserService userService,
                          ProjectService projectService) {
        this.messageRepo    = messageRepo;
        this.userService    = userService;
        this.projectService = projectService;
    }

    public Message send(String senderEmail, Long receiverId, String content, Long projectId) {
        User sender   = userService.findByEmail(senderEmail);
        User receiver = userService.findById(receiverId);
        Message.Builder builder = Message.builder()
                .sender(sender).receiver(receiver).content(content);
        if (projectId != null) {
            builder.project(projectService.getById(projectId));
        }
        return messageRepo.save(builder.build());
    }

    public List<Message> getConversation(String userEmail, Long otherUserId) {
        User user = userService.findByEmail(userEmail);
        return messageRepo.findConversation(user.getId(), otherUserId);
    }

    public void markRead(Long messageId, String readerEmail) {
        Message msg = messageRepo.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));
        if (msg.getReceiver().getEmail().equals(readerEmail)) {
            msg.setIsRead(true);
            messageRepo.save(msg);
        }
    }

    public List<Long> getConversationPartners(String userEmail) {
        User user = userService.findByEmail(userEmail);
        return messageRepo.findConversationPartnerIds(user.getId());
    }
}
