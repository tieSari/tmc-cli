package wad.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.domain.Guest;

@Controller
@RequestMapping("/guests")
public class GuestController {

    private final List<Guest> guests = new ArrayList<>();

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("guests", guests);
        return "/WEB-INF/views/page.jsp";
    }

}
