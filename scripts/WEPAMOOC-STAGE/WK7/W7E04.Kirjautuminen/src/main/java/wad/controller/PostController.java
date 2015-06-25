package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.domain.Post;
import wad.repository.PostRepository;

@Controller
@RequestMapping("/posts")
public class PostController {
    
    @Autowired
    private PostRepository postRepository;
    
    @RequestMapping(method = RequestMethod.POST)
    public String create(@ModelAttribute Post post) {
        // TODO: add person details in the future
        
        postRepository.save(post);
        return "redirect:/index";
    }
}
