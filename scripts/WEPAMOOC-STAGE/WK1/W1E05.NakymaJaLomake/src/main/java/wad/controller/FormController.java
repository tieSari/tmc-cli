package wad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FormController {

    private String inMemory = "nothing..";

    @RequestMapping("*")
    public String view(Model model) {
        model.addAttribute("data", inMemory);
        return "/WEB-INF/views/page.jsp";
    }
}
