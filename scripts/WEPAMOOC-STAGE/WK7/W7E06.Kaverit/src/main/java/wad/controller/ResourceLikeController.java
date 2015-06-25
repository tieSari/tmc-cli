package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.service.LikeService;

@Controller
@RequestMapping("/likes")
public class ResourceLikeController {

    @Autowired
    private LikeService likeService;

    @RequestMapping(method = RequestMethod.POST)
    public String addLike(@RequestParam String resourceId) {
        likeService.addLike(resourceId);
        return "redirect:/index";
    }
}
