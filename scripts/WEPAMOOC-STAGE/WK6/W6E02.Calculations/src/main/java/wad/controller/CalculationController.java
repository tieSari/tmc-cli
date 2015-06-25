package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.domain.Calculation;
import wad.repository.CalculationRepository;
import wad.service.CalculationService;

@Controller
@RequestMapping("/calculations")
public class CalculationController {

    @Autowired
    private CalculationRepository calculationRepository;

    @Autowired
    private CalculationService calculationService;

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        model.addAttribute("calculations", calculationRepository.findAll());
        return "calculations";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("calculation", calculationRepository.findOne(id));
        return "calculation";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(RedirectAttributes redirectAttributes,
            @ModelAttribute Calculation calculation) {

        calculation = calculationService.process(calculation);

        redirectAttributes.addAttribute("id", calculation.getId());
        return "redirect:/calculations/{id}";
    }
}
