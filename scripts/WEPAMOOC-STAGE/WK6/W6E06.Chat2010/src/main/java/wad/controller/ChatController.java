package wad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, @RequestParam("name") String username, @RequestParam("channel") String channel) {
        model.addAttribute("channel", channel);
        model.addAttribute("username", username);
        return "chat";
    }

}
