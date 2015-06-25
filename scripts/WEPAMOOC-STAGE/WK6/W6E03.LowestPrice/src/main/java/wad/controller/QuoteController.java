package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wad.service.QuoteService;
import wad.service.ext.BaseService;

@Controller
@RequestMapping("/quotes")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String getQuote(RedirectAttributes redirectAttributes,
            @RequestParam String item) {
        
        BaseService service = quoteService.getLowestPriceService(item);
        
        redirectAttributes.addFlashAttribute("item", item);
        redirectAttributes.addFlashAttribute("price", service.getLowestPrice(item));
        redirectAttributes.addFlashAttribute("service", service.getName());

        return "redirect:/quotes";
    }
}
