package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.Actor;
import wad.service.ActorService;
import wad.service.MovieService;

@Controller
@RequestMapping("actors")
public class ActorController {

    @Autowired
    private ActorService actorService;
    @Autowired
    private MovieService movieService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("actors", actorService.list());
        return "/WEB-INF/views/actors.jsp";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@RequestParam String name) {
        actorService.add(name);
        return "redirect:/actors";
    }

    @RequestMapping(value = "{actorId}", method = RequestMethod.GET)
    public String view(Model model, @PathVariable(value = "actorId") Long actorId) {
        model.addAttribute("actor", actorService.findById(actorId));
        model.addAttribute("movies", movieService.listMoviesWithout(actorId));
        return "/WEB-INF/views/actor.jsp";
    }

    @RequestMapping(value = "{actorId}", method = RequestMethod.DELETE)
    public String remove(@PathVariable(value = "actorId") Long actorId) {
        Actor actor = actorService.findById(actorId);
        if (!actor.getName().equals("Chuck Norris")) {
            actorService.remove(actorId);
        }

        return "redirect:/actors";
    }

    @RequestMapping(value = "{actorId}/movies", method = RequestMethod.POST)
    public String addActorToMovie(@PathVariable(value = "actorId") Long actorId,
            @RequestParam(value = "movieId") Long movieId) {
        actorService.addActorToMovie(actorId, movieId);
        return "redirect:/actors";
    }
}
