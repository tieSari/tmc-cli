package wad.controller;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.domain.Message;
import wad.repository.MessageRepository;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostConstruct
    public void init() {
        Message msg = new Message();
        msg.setContent("Jeff Davis");
        messageRepository.save(msg);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        return "messages";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@ModelAttribute Message message) {
        if (message.getContent() != null && !message.getContent().isEmpty()) {
            messageRepository.save(message);
        }

        return "redirect:/messages";
    }
}
