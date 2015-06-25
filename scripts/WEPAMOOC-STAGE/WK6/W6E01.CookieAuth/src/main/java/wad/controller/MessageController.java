package wad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @RequestMapping(method = RequestMethod.GET)
    public String getPage() {
        return "index";
    }
}
