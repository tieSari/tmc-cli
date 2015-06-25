package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.service.MovieService;

@Controller
@RequestMapping("movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("movies", movieService.list());
        return "/WEB-INF/views/movies.jsp";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@RequestParam String name, @RequestParam Integer lengthInMinutes) {
        movieService.add(name, lengthInMinutes);
        return "redirect:/movies";
    }

    @RequestMapping(value = "/{movieId}", method = RequestMethod.DELETE)
    public String add(@PathVariable(value = "movieId") Long movieId) {
        movieService.remove(movieId);
        return "redirect:/movies";
    }
}
