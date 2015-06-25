package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wad.domain.Twiiter;
import wad.repository.TwiitRepository;
import wad.repository.TwiiterRepository;

@Controller
@RequestMapping("/twiiters")
public class TwiiterController {

    @Autowired
    private TwiiterRepository twiiterRepository;

    @Autowired
    private TwiitRepository twiitRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String listTwiits(@PathVariable Long id, Model model) {
        Twiiter twiiter = twiiterRepository.findOne(id);

        model.addAttribute("twiiter", twiiter);

        Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, "created");
        model.addAttribute("twiits", twiitRepository.findByTwiiter(twiiter, pageable));

        return "twiits";
    }
}
