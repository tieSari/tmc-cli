package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.domain.Location;
import wad.repository.LocationRepository;

@Controller
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
model.addAttribute("locations", locationRepository.findAll());
        return "locations";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String view(Model model, @PathVariable Long id) {
model.addAttribute("location", locationRepository.findOne(id));
        return "location";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@ModelAttribute Location location) {
        locationRepository.save(location);
        return "redirect:/locations";
    }
}
