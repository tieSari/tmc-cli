package wad.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.domain.Post;
import wad.repository.PersonRepository;
import wad.repository.PostRepository;
import wad.service.LikeService;

@Controller
@RequestMapping("*")
public class DefaultController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LikeService likeService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String viewLogin(Model model) {
        return "login";
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String viewSignup(Model model) {
        return "signup";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        PageRequest pr = new PageRequest(0, 10, Sort.Direction.DESC, "date");

        List<Post> posts = postRepository.findAll(pr).getContent();
        List<String> resourceIds = new ArrayList<>();
        for (Post post : posts) {
            resourceIds.add(post.getId());
        }

        model.addAttribute("posts", posts);

        model.addAttribute("likes", likeService.getLikeCounts(resourceIds));

        pr = new PageRequest(0, 10, Sort.Direction.DESC, "lastUpdated");
        model.addAttribute("users", personRepository.findAll(pr).getContent());

        return "index";
    }
}
