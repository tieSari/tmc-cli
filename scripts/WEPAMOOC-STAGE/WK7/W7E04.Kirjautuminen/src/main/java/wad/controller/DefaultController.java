package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.repository.PersonRepository;
import wad.repository.PostRepository;

@Controller
@RequestMapping("*")
public class DefaultController {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        PageRequest pr = new PageRequest(0, 10, Sort.Direction.DESC, "date");

        model.addAttribute("posts", postRepository.findAll(pr).getContent());

        pr = new PageRequest(0, 10, Sort.Direction.DESC, "lastUpdated");
        model.addAttribute("users", personRepository.findAll(pr).getContent());

        return "index";
    }
}
