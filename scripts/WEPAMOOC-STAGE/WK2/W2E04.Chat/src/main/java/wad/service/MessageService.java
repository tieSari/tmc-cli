package wad.service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.domain.Message;
import wad.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    public List<Message> list() {
        return messageRepository.findAll();
    }

    @Transactional
    public void addMessage(Message message) {
        message.setPerson("You");
        messageRepository.save(message);
    }
}
