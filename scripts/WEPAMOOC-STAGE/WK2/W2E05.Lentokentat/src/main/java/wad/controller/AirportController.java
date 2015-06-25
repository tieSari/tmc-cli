package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.domain.Airport;
import wad.repository.AirportRepository;

@Controller
@RequestMapping("airports")
public class AirportController {

    @Autowired
    private AirportRepository airportRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("airports", airportRepository.findAll());
        return "/WEB-INF/views/airports.jsp";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@ModelAttribute Airport airport) {
        airportRepository.save(airport);
        return "redirect:/airports";
    }
}
