package wad.controller;

import wad.domain.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.service.LoveService;

@Controller
@RequestMapping("/loves")
public class LoveController {


    @ModelAttribute("pair")
    private Pair getPair() {
        return new Pair();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get() {
        return "/WEB-INF/views/loves.jsp";
    }

}
